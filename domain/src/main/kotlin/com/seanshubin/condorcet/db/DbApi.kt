package com.seanshubin.condorcet.db

interface DbApi : TransactionFunction {
    fun searchUserByName(userName: String): DbUser?
    fun searchUserByEmail(userEmail: String): DbUser?
    fun createUser(userName: String, userEmail: String, userPassword: String): DbUser
    fun findElectionByName(electionName: String): DbElection
    fun searchElectionByName(electionName: String): DbElection?
    fun createElection(userName: String, electionName: String): DbElection
    fun updateElection(election: DbElection): DbElection
    fun listCandidateNames(electionName: String): List<String>
    fun listVoterNames(electionName: String): List<String>
}
