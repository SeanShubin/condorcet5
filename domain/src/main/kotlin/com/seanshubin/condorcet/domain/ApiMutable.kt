package com.seanshubin.condorcet.domain

import java.time.Instant

interface ApiMutable {
    // auth
    fun register(name: String, email: String, password: String): Credentials

    // election
    fun createElection(credentials: Credentials, electionName: String): ElectionDetail

    fun setEndDate(credentials: Credentials, electionName: String, endDate: Instant?): ElectionDetail
    fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail
    fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail
    fun endElection(credentials: Credentials, electionName: String): ElectionDetail
    fun setCandidateNames(credentials: Credentials, electionName: String, candidateNames: List<String>): ElectionDetail
    fun setVoters(credentials: Credentials,
                  electionName: String,
                  eligibleVoterNames: List<String>): ElectionDetail

    fun setVotersToAll(credentials: Credentials, electionName: String): ElectionDetail
    fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail

    // ballot
    fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: Map<String, Int>): Ballot
}
