package com.seanshubin.condorcet.domain

interface ApiReadOnly {
    // auth
    fun login(nameOrEmail: String, password: String): Credentials

    // election

    fun listElections(credentials: Credentials): List<ElectionSummary>
    fun getElection(credentials: Credentials, electionName: String): ElectionDetail

    // ballot
    fun listBallots(credentials: Credentials, voterName: String): List<Ballot>

    fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot

    // tally
    fun tally(credentials: Credentials, electionName: String): Tally
}
