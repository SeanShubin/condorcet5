package com.seanshubin.condorcet.db

interface DbApi : TransactionFunction {
    // queries
    fun findUserByName(userName: String): DbUser
    fun searchUserByName(userName: String): DbUser?
    fun searchUserByEmail(userEmail: String): DbUser?
    fun findElectionByName(electionName: String): DbElection
    fun searchElectionByName(electionName: String): DbElection?
    fun listCandidateNames(electionName: String): List<String>
    fun listVoterNames(electionName: String): List<String>

    // commands
    fun createUser(userName: String, userEmail: String, userPassword: String)
    fun createElection(userName: String, electionName: String): DbElection
    fun setCandidates(electionName: String, candidateNames: List<String>)
    fun setElectionEndDate(electionName: String, endDate: String?)
    fun setElectionSecretBallot(electionName: String, secretBallot: Boolean)
    fun setElectionStatus(electionName: String, status: DbStatus)
}
