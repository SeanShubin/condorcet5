package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.crypto.PasswordUtil
import com.seanshubin.condorcet.crypto.SaltAndHash
import com.seanshubin.condorcet.db.DbApi
import com.seanshubin.condorcet.db.DbElection
import com.seanshubin.condorcet.db.DbStatus
import java.time.Clock
import java.time.Instant
import java.time.format.DateTimeParseException

class ApiBackedByDb(private val db: DbApi,
                    private val clock: Clock,
                    private val passwordUtil: PasswordUtil) : Api {
    override fun login(nameOrEmail: String, password: String): Credentials {
        val trimmedUserNameOrUserEmail = trim(nameOrEmail)
        val dbUser =
                db.searchUserByName(trimmedUserNameOrUserEmail) ?: db.searchUserByEmail(trimmedUserNameOrUserEmail)
                ?: throw RuntimeException("User with name or email '$trimmedUserNameOrUserEmail' does not exist")
        val givenCredentials = Credentials(dbUser.name, password)
        assertCredentialsValid(givenCredentials)
        val actualCredentials = Credentials(dbUser.name, password)
        return actualCredentials
    }

    override fun register(name: String, email: String, password: String): Credentials {
        val trimmedUserName = trim(name)
        val trimmedUserEmail = trim(email)
        assertUserNameDoesNotExist(trimmedUserName)
        assertUserEmailDoesNotExist(trimmedUserEmail)
        val (salt, hash) = passwordUtil.createSaltAndHash(password)
        db.createUser(trimmedUserName, trimmedUserEmail, salt, hash)
        val dbUser = db.findUserByName(trimmedUserName)
        return Credentials(dbUser.name, password)
    }

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail {
        val trimmedElectionName = trim(electionName)
        assertCredentialsValid(credentials)
        assertElectionNameDoesNotExist(trimmedElectionName)
        val dbElection = db.createElection(credentials.userName, trimmedElectionName)
        return dbElection.toApiElectionDetail()
    }

    override fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail {
        return withAllowedToEdit(credentials, electionName) { election ->
            val electionEnd = election.end
            if (electionEnd == null) {
                db.setElectionStatus(election.name, DbStatus.LIVE)
            } else {
                val electionEndInstant = Instant.parse(electionEnd)
                val nowInstant = clock.instant()
                if (electionEndInstant.isBefore(nowInstant)) {
                    throw RuntimeException(
                            "Unable to start election now ($nowInstant), " +
                                    "its end date ($electionEndInstant) " +
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
                DbStatus.LIVE -> db.setElectionStatus(election.name, DbStatus.COMPLETE)
                DbStatus.COMPLETE -> throw RuntimeException(
                        "Can not end election '${election.name}', it is already complete")
            }
        }
    }

    override fun updateCandidateNames(credentials: Credentials,
                                      electionName: String,
                                      candidateNames: List<String>): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                val cleanCandidates = candidateNames.map(::trim).distinctBy { it.toLowerCase() }
                db.setCandidates(election.name, cleanCandidates)
            }

    override fun updateEligibleVoters(credentials: Credentials,
                                      electionName: String,
                                      eligibleVoterNames: List<String>): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                val cleanVoters = eligibleVoterNames.map(::trim).distinctBy { it.toLowerCase() }
                db.setVoters(election.name, cleanVoters)
            }

    override fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                db.setVotersToAll(election.name)
            }

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail {
        assertElectionNameDoesNotExist(newElectionName)
        return withValidCredentialsAndElection(credentials, electionToCopyName) { election ->
            db.createElection(credentials.userName, newElectionName)
            db.setElectionSecretBallot(newElectionName, election.secret)
            db.setCandidates(newElectionName, db.listCandidateNames(electionToCopyName))
            db.setVoters(newElectionName, db.listVoterNames(electionToCopyName))
            db.findElectionByName(newElectionName).toApiElectionDetail()
        }
    }

    override fun listElections(credentials: Credentials): List<ElectionSummary> {
        TODO("not implemented")
    }

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail =
            withValidCredentialsAndElection(credentials, electionName) { election ->
                election.toApiElectionDetail()
            }

    override fun listBallots(credentials: Credentials, voterName: String): List<Ballot> {
        TODO("not implemented")
    }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot {
        TODO("not implemented")
    }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: List<Ranking>): Ballot {
        TODO("not implemented")
    }

    override fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                assertValidIsoDateTimeOrNull(isoEndDate)
                db.setElectionEndDate(election.name, isoEndDate)
            }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail =
            withAllowedToEdit(credentials, electionName) { election ->
                db.setElectionSecretBallot(election.name, secretBallot)
            }

    override fun tally(credentials: Credentials, electionName: String): Tally {
        TODO("not implemented")
    }

    private fun assertUserNameDoesNotExist(userName: String) {
        if (db.searchUserByName(userName) != null) throw RuntimeException("User with name '$userName' already exists")
    }

    private fun assertUserEmailDoesNotExist(userEmail: String) {
        if (db.searchUserByEmail(userEmail) != null) throw RuntimeException("User with email '$userEmail' already exists")
    }

    private fun assertCredentialsValid(credentials: Credentials) {
        val user = db.searchUserByName(credentials.userName) ?: authError(credentials)
        val saltAndHash = SaltAndHash(user.salt, user.hash)
        if (passwordUtil.validatePassword(credentials.userPassword, saltAndHash)) authError(credentials)
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
        val voterNames = db.listVoterNames(name)
        val isAllVoters = db.electionHasAllVoters(name)
        return ElectionDetail(owner, name, end, secret, status.toApiStatus(), candidateNames, voterNames, isAllVoters)
    }

    private fun DbStatus.toApiStatus(): ElectionStatus = when (this) {
        DbStatus.EDITING -> ElectionStatus.EDITING
        DbStatus.LIVE -> ElectionStatus.LIVE
        DbStatus.COMPLETE -> ElectionStatus.COMPLETE
    }

    private fun assertValidIsoDateTimeOrNull(s: String?) {
        if (s != null) {
            assertValidIsoDateTime(s)
        }
    }

    private fun assertValidIsoDateTime(s: String) {
        try {
            Instant.parse(s)
        } catch (ex: DateTimeParseException) {
            throw RuntimeException("Unable to parse '$s' into an ISO date time")
        }
    }

    private fun trim(s: String): String = s.trim().replace(whitespaceBlock, " ")

    private fun <T> withValidCredentials(credentials: Credentials, f: () -> T): T {
        val user = db.searchUserByName(credentials.userName) ?: authError(credentials)
        assertCredentialsValid(credentials)
        return f()
    }

    private fun <T> withValidCredentialsAndElection(credentials: Credentials,
                                                    electionName: String,
                                                    f: (DbElection) -> T): T =
            withValidCredentials(credentials) {
                val election = db.findElectionByName(trim(electionName))
                f(election)
            }

    private fun withAllowedToEdit(credentials: Credentials, electionName: String, f: (DbElection) -> Unit): ElectionDetail =
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

    companion object {
        private val whitespaceBlock = Regex("""\s+""")
    }
}
