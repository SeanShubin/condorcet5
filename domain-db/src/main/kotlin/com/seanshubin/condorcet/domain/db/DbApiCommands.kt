package com.seanshubin.condorcet.domain.db

import java.time.Instant

interface DbApiCommands {
    fun createUser(name: String, email: String, salt: String, hash: String)

    fun createElection(ownerUserName: String, electionName: String)
    fun setElectionEndDate(electionName: String, end: Instant?)
    fun setElectionSecretBallot(electionName: String, secretBallot: Boolean)
    fun setElectionStatus(electionName: String, status: DbStatus)
    fun setCandidates(electionName: String, candidateNames: List<String>)
    fun setVoters(electionName: String, voterNames: List<String>)
    fun setVotersToAll(electionName: String)
    fun createBallot(electionName: String,
                     userName: String,
                     confirmation: String,
                     whenCast: Instant,
                     rankings: Map<String, Int>)

    fun updateBallot(electionName: String,
                     userName: String,
                     whenCast: Instant,
                     rankings: Map<String, Int>)

    fun setTally(electionName: String, report: String)
}
