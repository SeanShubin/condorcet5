package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.domain.db.Ballot
import com.seanshubin.condorcet.domain.db.Report
import java.time.Instant

interface Api {
    // auth
    fun login(nameOrEmail: String, password: String): Credentials

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
    fun listElections(credentials: Credentials): List<ElectionSummary>
    fun getElection(credentials: Credentials, electionName: String): ElectionDetail
    fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail

    // ballot
    fun listBallots(credentials: Credentials, voterName: String): List<Ballot>

    fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot
    fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: Map<String, Int>): Ballot

    // tally
    fun tally(credentials: Credentials, electionName: String): Report
}
