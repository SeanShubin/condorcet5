package com.seanshubin.condorcet.domain

interface Api {
    // auth
    fun login(nameOrEmail: String, password: String): Credentials
    fun register(name: String, email: String, password: String): Credentials

    // election
    fun createElection(credentials: Credentials, electionName: String): ElectionDetail
    fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): ElectionDetail
    fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail
    fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail
    fun endElection(credentials: Credentials, electionName: String): ElectionDetail
    fun updateCandidateNames(credentials: Credentials, electionName: String, candidateNames: List<String>): ElectionDetail
    fun updateEligibleVoters(credentials: Credentials,
                             electionName: String,
                             eligibleVoterNames: List<String>): ElectionDetail
    fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): ElectionDetail
    fun listElections(credentials: Credentials): List<ElectionSummary>
    fun getElection(credentials: Credentials, electionName: String): ElectionDetail
    fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail

    // ballot
    fun listBallots(credentials: Credentials, voterName: String): List<Ballot>
    fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot
    fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: Map<String, Int>): Ballot

    // tally
    fun tally(credentials: Credentials, electionName: String): Tally
}
