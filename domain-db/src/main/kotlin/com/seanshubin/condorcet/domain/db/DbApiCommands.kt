package com.seanshubin.condorcet.domain.db

import java.time.Instant

interface DbApiCommands {
    fun createUser(initiator: Initiator,
                   name: String,
                   email: String,
                   salt: String,
                   hash: String)

    fun createElection(initiator: Initiator,
                       ownerUserName: String,
                       electionName: String)

    fun setElectionEndDate(initiator: Initiator,
                           electionName: String,
                           end: Instant?)

    fun setElectionSecretBallot(initiator: Initiator,
                                electionName: String,
                                secretBallot: Boolean)

    fun setElectionStatus(initiator: Initiator,
                          electionName: String,
                          status: DbStatus)

    fun setCandidates(initiator: Initiator,
                      electionName: String,
                      candidateNames: List<String>)

    fun setVoters(initiator: Initiator,
                  electionName: String,
                  voterNames: List<String>)

    fun setVotersToAll(initiator: Initiator,
                       electionName: String)

    fun createBallot(initiator: Initiator,
                     electionName: String,
                     userName: String,
                     confirmation: String,
                     whenCast: Instant,
                     rankings: Map<String, Int>)

    fun updateBallot(initiator: Initiator,
                     electionName: String,
                     userName: String,
                     whenCast: Instant,
                     rankings: Map<String, Int>)

    fun setTally(initiator: Initiator,
                 electionName: String,
                 report: String)
}
