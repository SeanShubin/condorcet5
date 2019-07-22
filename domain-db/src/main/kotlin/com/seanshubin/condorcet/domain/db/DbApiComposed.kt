package com.seanshubin.condorcet.domain.db

import java.time.Instant

class DbApiComposed(private val queries: DbApiQueries,
                    private val commands: DbApiCommands) : DbApi {
    override fun findUserByName(user: String): DbUser = queries.findUserByName(user)

    override fun searchUserByName(user: String): DbUser? = queries.searchUserByName(user)

    override fun searchUserByEmail(email: String): DbUser? = queries.searchUserByEmail(email)

    override fun findElectionByName(name: String): DbElection = queries.findElectionByName(name)

    override fun searchElectionByName(name: String): DbElection? = queries.searchElectionByName(name)

    override fun listCandidateNames(election: String): List<String> = queries.listCandidateNames(election)

    override fun listEligibleVoterNames(election: String): List<String> = queries.listEligibleVoterNames(election)

    override fun electionHasAllVoters(election: String): Boolean = queries.electionHasAllVoters(election)

    override fun searchBallot(election: String, user: String): DbBallot? = queries.searchBallot(election, user)

    override fun findBallot(election: String, user: String): DbBallot = queries.findBallot(election, user)

    override fun findTally(election: String): DbTally = queries.findTally(election)

    override fun searchTally(election: String): DbTally? = queries.searchTally(election)

    override fun listRankings(election: String, user: String): List<DbRanking> = queries.listRankings(election, user)

    override fun listBallotsForElection(election: String): List<DbBallot> = queries.listBallotsForElection(election)

    override fun listBallotsForVoter(voter: String): List<DbBallot> = queries.listBallotsForVoter(voter)

    override fun listElections(): List<DbElection> = queries.listElections()

    override fun lastEventSynced(): Int = queries.lastEventSynced()

    override fun createUser(initiator: Initiator, name: String, email: String, salt: String, hash: String) {
        commands.createUser(initiator, name, email, salt, hash)
    }

    override fun createElection(initiator: Initiator, ownerUserName: String, electionName: String) {
        commands.createElection(initiator, ownerUserName, electionName)
    }

    override fun setElectionEndDate(initiator: Initiator, electionName: String, end: Instant?) {
        commands.setElectionEndDate(initiator, electionName, end)
    }

    override fun setElectionSecretBallot(initiator: Initiator, electionName: String, secretBallot: Boolean) {
        commands.setElectionSecretBallot(initiator, electionName, secretBallot)
    }

    override fun setElectionStatus(initiator: Initiator, electionName: String, status: DbStatus) {
        commands.setElectionStatus(initiator, electionName, status)
    }

    override fun setCandidates(initiator: Initiator, electionName: String, candidateNames: List<String>) {
        commands.setCandidates(initiator, electionName, candidateNames)
    }

    override fun setVoters(initiator: Initiator, electionName: String, voterNames: List<String>) {
        commands.setVoters(initiator, electionName, voterNames)
    }

    override fun setVotersToAll(initiator: Initiator, electionName: String) {
        commands.setVotersToAll(initiator, electionName)
    }

    override fun createBallot(initiator: Initiator,
                              electionName: String,
                              userName: String,
                              confirmation: String,
                              whenCast: Instant,
                              rankings: Map<String, Int>) {
        commands.createBallot(initiator, electionName, userName, confirmation, whenCast, rankings)
    }

    override fun updateBallot(initiator: Initiator,
                              electionName: String,
                              userName: String,
                              whenCast: Instant,
                              rankings: Map<String, Int>) {
        commands.updateBallot(initiator, electionName, userName, whenCast, rankings)
    }

    override fun setReport(initiator: Initiator, electionName: String, report: Report) {
        commands.setReport(initiator, electionName, report)
    }
}
