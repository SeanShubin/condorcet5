package com.seanshubin.condorcet.domain

interface Api {
    // auth
    fun login(userNameOrUserEmail: String, userPassword: String): Credentials
    fun register(userName: String, userEmail: String, userPassword: String): Credentials

    // election
    fun createElection(credentials: Credentials, electionName: String): ElectionDetail
    fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): ElectionDetail
    fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail
    fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail
    fun listElections(credentials: Credentials): List<ElectionSummary>
    fun getElection(credentials: Credentials, electionName: String): ElectionDetail
    fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail
    fun endElection(credentials: Credentials, electionName: String): ElectionDetail
    fun listCandidates(credentials: Credentials, electionName: String): List<String>
    fun updateCandidateNames(credentials: Credentials, electionName: String, candidateNames: List<String>): ElectionDetail
    fun listEligibleVoters(credentials: Credentials, electionName: String): List<String>
    fun areAllVotersEligible(credentials: Credentials, electionName: String):Boolean
    fun listAllVoters(credentials: Credentials): List<String>
    fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoterNames: List<String>): List<String>
    fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): List<String>

    // ballot
    fun listBallots(credentials: Credentials, voterName: String): List<Ballot>
    fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot
    fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: List<Ranking>): Ballot

    // tally
    fun tally(credentials: Credentials, electionName: String): Tally
}
