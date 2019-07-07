package com.seanshubin.condorcet.domain.db

import java.time.Instant

interface DbApi {
    // queries
    fun findUserByName(user: String): DbUser

    fun searchUserByName(user: String): DbUser?
    fun searchUserByEmail(email: String): DbUser?
    fun findElectionByName(name: String): DbElection
    fun searchElectionByName(name: String): DbElection?
    fun listCandidateNames(election: String): List<String>
    fun listVoterNames(election: String): List<String>
    fun electionHasAllVoters(election: String): Boolean
    fun searchBallot(election: String, user: String): DbBallot?
    fun findBallot(election: String, user: String): DbBallot
    fun listTally(election: String): List<DbTally>
    fun listRankings(election: String, user: String): List<DbRanking>
    fun listBallotsForElection(election: String): List<DbBallot>
    fun listBallotsForVoter(voter: String): List<DbBallot>
    fun listElections(): List<DbElection>

    // commands
    fun createUser(name: String, email: String, salt: String, hash: String)

    fun createElection(owner: String, name: String)
    fun setElectionEndDate(electionName: String, endDate: Instant?)
    fun setElectionSecretBallot(electionName: String, secretBallot: Boolean)
    fun setElectionStatus(electionName: String, status: DbStatus)
    fun setCandidates(electionName: String, candidateNames: List<String>)
    fun setVoters(electionName: String, voterNames: List<String>)
    fun setVotersToAll(electionName: String)
    fun createBallot(electionName: String,
                     userName: String,
                     confirmation: String,
                     whenCast: Instant,
                     rankings: Map<String, Int>)

    fun updateBallot(electionName: String,
                     userName: String,
                     whenCast: Instant,
                     rankings: Map<String, Int>)

    fun setTally(electionName: String, rankings: Map<String, Int>)
}
