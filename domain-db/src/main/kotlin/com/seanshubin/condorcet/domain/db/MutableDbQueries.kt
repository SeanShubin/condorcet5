package com.seanshubin.condorcet.domain.db

interface MutableDbQueries {
    fun lastSynced(): Int
    fun findUserByName(user: String): DbUser
    fun searchUserByName(user: String): DbUser?
    fun searchUserByEmail(email: String): DbUser?
    fun findElectionByName(name: String): DbElection
    fun searchElectionByName(name: String): DbElection?
    fun listCandidateNames(election: String): List<String>
    fun listEligibleVoterNames(election: String): List<String>
    fun electionHasAllVoters(election: String): Boolean
    fun searchBallot(election: String, user: String): DbBallot?
    fun findBallot(election: String, user: String): DbBallot
    fun findTally(election: String): DbTally
    fun searchTally(election: String): DbTally?
    fun listRankings(election: String, user: String): List<DbRanking>
    fun listBallotsForElection(election: String): List<DbBallot>
    fun listBallotsForVoter(voter: String): List<DbBallot>
    fun listElections(): List<DbElection>
}
