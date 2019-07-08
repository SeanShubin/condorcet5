package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.algorithm.CondorcetAlgorithm
import com.seanshubin.condorcet.algorithm.TallyElectionRequest
import com.seanshubin.condorcet.crypto.PasswordUtil
import com.seanshubin.condorcet.crypto.SaltAndHash
import com.seanshubin.condorcet.crypto.UniqueIdGenerator
import com.seanshubin.condorcet.domain.AlgorithmToDomain.toDomain
import com.seanshubin.condorcet.domain.Ranking.Companion.unbiasedSort
import com.seanshubin.condorcet.domain.db.*
import com.seanshubin.condorcet.json.JsonUtil.jsonMapper
import com.seanshubin.condorcet.table.formatter.ListUtil.exactlyOne
import java.time.Clock
import java.time.Instant
import java.util.*
import com.seanshubin.condorcet.algorithm.Ballot as AlgorithmBallot


class ApiBackedByDb(private val db: DbApi,
                    private val clock: Clock,
                    private val passwordUtil: PasswordUtil,
                    private val uniqueIdGenerator: UniqueIdGenerator,
                    private val random: Random) : Api {
    override fun login(nameOrEmail: String, password: String): Credentials {
        val dbUser =
                db.searchUserByName(nameOrEmail) ?: db.searchUserByEmail(nameOrEmail)
                ?: throw RuntimeException("User with name or email '$nameOrEmail' does not exist")
        val givenCredentials = Credentials(dbUser.name, password)
        assertCredentialsValid(givenCredentials)
        val actualCredentials = Credentials(dbUser.name, password)
        return actualCredentials
    }

    override fun register(name: String, email: String, password: String): Credentials {
        assertUserNameDoesNotExist(name)
        assertUserEmailDoesNotExist(email)
        val (salt, hash) = passwordUtil.createSaltAndHash(password)
        db.createUser(name, email, salt, hash)
        val dbUser = db.findUserByName(name)
        return Credentials(dbUser.name, password)
    }

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail {
        assertCredentialsValid(credentials)
        assertElectionNameDoesNotExist(electionName)
        db.createElection(credentials.userName, electionName)
        val dbElection = db.findElectionByName(electionName)
        return dbElection.toApiElectionDetail()
    }

    override fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail {
        return withAllowedToEdit(credentials, electionName) { election ->
            val electionEnd = election.end
            if (electionEnd == null) {
                db.setElectionStatus(election.name, DbStatus.LIVE)
            } else {
                val now = clock.instant()
                if (electionEnd.isBefore(now)) {
                    throw RuntimeException(
                            "Unable to start election now ($now), " +
                                    "its end date ($electionEnd) " +
                                    "has already passed")
                }
            }
        }
    }

    override fun endElection(credentials: Credentials, electionName: String): ElectionDetail {
        return withAllowedToEdit(credentials, electionName) { election ->
            when (election.status) {
                DbStatus.EDITING -> throw RuntimeException(
                        "Can not end election '${election.name}', it is not live")
                DbStatus.LIVE -> {
                    db.setElectionStatus(election.name, DbStatus.COMPLETE)
                    updateElectionTally(electionName)
                }
                DbStatus.COMPLETE -> throw RuntimeException(
                        "Can not end election '${election.name}', it is already complete")
            }
        }
    }

    override fun setCandidateNames(credentials: Credentials,
                                   electionName: String,
                                   candidateNames: List<String>): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                db.setCandidates(election.name, candidateNames)
            }

    override fun setVoters(credentials: Credentials,
                           electionName: String,
                           eligibleVoterNames: List<String>): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                db.setVoters(election.name, eligibleVoterNames)
            }

    override fun setVotersToAll(credentials: Credentials, electionName: String): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                db.setVotersToAll(election.name)
            }

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail {
        assertElectionNameDoesNotExist(newElectionName)
        return withValidCredentialsAndElection(credentials, electionToCopyName) { election ->
            db.createElection(credentials.userName, newElectionName)
            db.setElectionSecretBallot(newElectionName, election.secret)
            db.setCandidates(newElectionName, db.listCandidateNames(electionToCopyName))
            db.setVoters(newElectionName, db.listEligibleVoterNames(electionToCopyName))
            db.findElectionByName(newElectionName).toApiElectionDetail()
        }
    }

    override fun listElections(credentials: Credentials): List<ElectionSummary> =
            withValidCredentials(credentials) {
                db.listElections().map { it.toApiElectionSummary() }
            }

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail =
            withValidCredentialsAndElection(credentials, electionName) { election ->
                election.toApiElectionDetail()
            }

    // todo: only list own ballots
    override fun listBallots(credentials: Credentials, voterName: String): List<Ballot> =
            withValidCredentials(credentials) {
                db.listBallotsForVoter(voterName).map { it.toApiBallot() }
            }

    // todo: only list own ballot
    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot =
            withValidCredentials(credentials) {
                db.findBallot(electionName, voterName).toApiBallot()
            }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: Map<String, Int>): Ballot =
            withAllowedToVote(credentials, electionName) { election ->
                val ballot = db.searchBallot(electionName, credentials.userName)
                val now = clock.instant()
                if (ballot == null) {
                    val confirmation = uniqueIdGenerator.uniqueId()
                    db.createBallot(electionName, credentials.userName, confirmation, now, rankings)
                } else {
                    db.updateBallot(electionName, credentials.userName, now, rankings)
                }
                db.findBallot(electionName, credentials.userName).toApiBallot(election)
            }

    override fun setEndDate(credentials: Credentials, electionName: String, endDate: Instant?): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                db.setElectionEndDate(election.name, endDate)
            }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                db.setElectionSecretBallot(election.name, secretBallot)
            }

    override fun tally(credentials: Credentials, electionName: String): Tally =
            withValidCredentialsAndElection(credentials, electionName) { election ->
                updateElectionTally(electionName)
                val dbTally = db.findTally(electionName)
                dbTally.toApiTally()
            }

    private fun updateElectionTally(electionName: String) {
        val dbElection = db.findElectionByName(electionName)
        if (dbElection.status != DbStatus.COMPLETE) {
            throw RuntimeException("Can not tally election $electionName, its status is ${dbElection.status}")
        }
        val originalDbTally = db.searchTally(electionName)
        if (originalDbTally != null) return
        val candidates = db.listCandidateNames(electionName).toSet()
        val eligibleVoters = db.listEligibleVoterNames(electionName).toSet()
        fun ballotToAlgorithm(ballot: DbBallot): AlgorithmBallot {
            val rankings = rankingsToAlgorithm(db.listRankings(electionName, ballot.user))
            return AlgorithmBallot(ballot.user, ballot.confirmation, rankings)
        }

        val dbBallots = db.listBallotsForElection(electionName)
        val dbBallotByConfirmation: Map<String, DbBallot> =
                dbBallots.groupBy { it.confirmation }.mapValues { it.value.exactlyOne() }
        val algorithmBallots = dbBallots.map(::ballotToAlgorithm)
        val request = TallyElectionRequest(
                electionName,
                candidates,
                eligibleVoters,
                algorithmBallots)
        val response = CondorcetAlgorithm.tally(request)
        val isActive = dbElection.status == DbStatus.LIVE
        val tally = Tally(
                response.election,
                dbElection.owner,
                response.candidates,
                response.voted,
                response.didNotVote,
                response.ballots.toDomain(dbBallotByConfirmation, isActive),
                response.preferenceMatrix,
                response.strongestPathMatrix,
                response.placings.toDomain()
        )
        db.setTally(electionName, jsonMapper.writeValueAsString(tally))
    }

    private fun rankingsToAlgorithm(rankings: List<DbRanking>): Map<String, Int> =
            rankings.map { ranking ->
                Pair(ranking.candidateName, ranking.rank)
            }.toMap()

    private fun assertUserNameDoesNotExist(userName: String) {
        if (db.searchUserByName(userName) != null) throw RuntimeException("User with name '$userName' already exists")
    }

    private fun assertUserEmailDoesNotExist(userEmail: String) {
        if (db.searchUserByEmail(userEmail) != null) throw RuntimeException("User with email '$userEmail' already exists")
    }

    private fun assertCredentialsValid(credentials: Credentials) {
        val user = db.searchUserByName(credentials.userName) ?: authError(credentials)
        val saltAndHash = SaltAndHash(user.salt, user.hash)
        if (!passwordUtil.validatePassword(credentials.userPassword, saltAndHash)) authError(credentials)
    }

    private fun assertElectionNameDoesNotExist(electionName: String) {
        val existingElection = db.searchElectionByName(electionName)
        if (existingElection != null) {
            throw RuntimeException("Election with name '${existingElection.name}' already exists")
        }
    }

    private fun authError(credentials: Credentials): Nothing =
            throw RuntimeException("Invalid user/password combination for '${credentials.userName}'")

    private fun DbElection.toApiElectionDetail(): ElectionDetail {
        val candidateNames = db.listCandidateNames(name)
        val voterNames = db.listEligibleVoterNames(name)
        val isAllVoters = db.electionHasAllVoters(name)
        return ElectionDetail(owner, name, end, secret, status.toApiStatus(), candidateNames, voterNames, isAllVoters)
    }

    private fun DbElection.toApiElectionSummary(): ElectionSummary {
        val electionDetail = toApiElectionDetail()
        return ElectionSummary(
                owner,
                name,
                end,
                secret,
                status.toApiStatus(),
                electionDetail.candidateNames.size,
                electionDetail.voterNames.size)
    }

    private fun DbBallot.toApiBallot(dbElection: DbElection): Ballot {
        val isActive = dbElection.status == DbStatus.LIVE
        val dbRankings = db.listRankings(election, user)
        val candidates = db.listCandidateNames(election)
        fun lookupRanking(candidate: String): Int? {
            val dbRanking = dbRankings.find { it.candidateName == candidate }
            return dbRanking?.rank
        }

        val rankings = candidates.map {
            Ranking(lookupRanking(it), it)
        }.unbiasedSort(random)
        return Ballot(
                user,
                election,
                confirmation,
                whenCast,
                isActive,
                rankings)
    }

    private fun DbBallot.toApiBallot(): Ballot {
        val election = db.findElectionByName(this.election)
        return toApiBallot(election)
    }

    private fun DbStatus.toApiStatus(): ElectionStatus = when (this) {
        DbStatus.EDITING -> ElectionStatus.EDITING
        DbStatus.LIVE -> ElectionStatus.LIVE
        DbStatus.COMPLETE -> ElectionStatus.COMPLETE
    }

    private fun DbTally.toApiTally(): Tally {
        val tally = jsonMapper.readValue(report, Tally::class.java)
        return tally
    }

    private fun <T> withValidCredentials(credentials: Credentials, f: () -> T): T {
        db.searchUserByName(credentials.userName) ?: authError(credentials)
        assertCredentialsValid(credentials)
        return f()
    }

    private fun <T> withValidCredentialsAndElection(credentials: Credentials,
                                                    electionName: String,
                                                    f: (DbElection) -> T): T =
            withValidCredentials(credentials) {
                val election = db.findElectionByName(electionName)
                f(election)
            }

    private fun withAllowedToEdit(credentials: Credentials,
                                  electionName: String,
                                  f: (DbElection) -> Unit): ElectionDetail =
            withValidCredentialsAndElection(credentials, electionName) { election ->
                if (election.owner == credentials.userName) {
                    f(election)
                    db.findElectionByName(election.name).toApiElectionDetail()
                } else {
                    throw RuntimeException(
                            "User '${credentials.userName}' " +
                                    "is not allowed to edit election '${election.name}' " +
                                    "owned by user '${election.owner}'")
                }
            }

    private fun <T> withAllowedToVote(credentials: Credentials,
                                      electionName: String,
                                      f: (DbElection) -> T): T =
            withValidCredentialsAndElection(credentials, electionName) { election ->
                if (election.status != DbStatus.LIVE) {
                    throw RuntimeException("Election $electionName is not live")
                }
                val voters = db.listEligibleVoterNames(election.name)
                if (!voters.contains(credentials.userName)) {
                    throw RuntimeException("User ${credentials.userName} is not allowed to vote in election ${election.name}")
                }
                f(election)
            }
}
