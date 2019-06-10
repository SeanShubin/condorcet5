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
    fun electionHasAllVoters(electionName: String): Boolean

    // commands
    fun createUser(userName: String, userEmail: String, userPassword: String)
    fun createElection(userName: String, electionName: String): DbElection
    fun setElectionEndDate(electionName: String, endDate: String?)
    fun setElectionSecretBallot(electionName: String, secretBallot: Boolean)
    fun setElectionStatus(electionName: String, status: DbStatus)
    fun setCandidates(electionName: String, candidateNames: List<String>)
    fun setVoters(electionName: String, voterNames: List<String>)
    fun setVotersToAll(electionName: String)
}
