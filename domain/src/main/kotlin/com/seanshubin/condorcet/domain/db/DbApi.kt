package com.seanshubin.condorcet.domain.db

interface DbApi {
    // queries
    fun findUserByName(userName: String): DbUser
    fun searchUserByName(userName: String): DbUser?
    fun searchUserByEmail(userEmail: String): DbUser?
    fun findElectionByName(electionName: String): DbElection
    fun searchElectionByName(electionName: String): DbElection?
    fun listCandidateNames(electionName: String): List<String>
    fun listVoterNames(electionName: String): List<String>
    fun electionHasAllVoters(electionName: String): Boolean
    fun searchBallot(electionName: String, userName: String): DbBallot?
    fun findBallot(electionName: String, userName: String): DbBallot
    fun listTally(electionName: String): List<DbTally>

    // commands
    fun createUser(name: String, email: String, salt: String, hash: String)

    fun createElection(owner: String, name: String)
    fun setElectionEndDate(electionName: String, endDate: String?)
    fun setElectionSecretBallot(electionName: String, secretBallot: Boolean)
    fun setElectionStatus(electionName: String, status: DbStatus)
    fun setCandidates(electionName: String, candidateNames: List<String>)
    fun setVoters(electionName: String, voterNames: List<String>)
    fun setVotersToAll(electionName: String)
    fun createBallot(electionName: String,
                     userName: String,
                     confirmation: String,
                     whenCast: String,
                     rankings: Map<String, Int>)

    fun updateBallot(electionName: String,
                     userName: String,
                     whenCast: String,
                     rankings: Map<String, Int>)

    fun setTally(electionName: String, rankings: Map<String, Int>)
}
