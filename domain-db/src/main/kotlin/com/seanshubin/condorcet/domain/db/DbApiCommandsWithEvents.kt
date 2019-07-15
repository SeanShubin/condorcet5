package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.json.JsonUtil.compact
import java.time.Clock
import java.time.Instant

class DbApiCommandsWithEvents(private val dbFromResource: DbFromResource,
                              private val clock: Clock) : DbApiCommands {
    override fun createUser(initiator: Initiator,
                            name: String,
                            email: String,
                            salt: String,
                            hash: String) {
        insertEvent(initiator, "CreateUser", Event.CreateUser(name, email, salt, hash))
    }

    override fun createElection(initiator: Initiator,
                                ownerUserName: String,
                                electionName: String) {
        insertEvent(initiator, "CreateElection", Event.CreateElection(ownerUserName, electionName))
    }

    override fun setElectionEndDate(initiator: Initiator,
                                    electionName: String,
                                    end: Instant?) {
        insertEvent(initiator, "SetElectionEndDate", Event.SetElectionEndDate(electionName, end))
    }

    override fun setElectionSecretBallot(initiator: Initiator,
                                         electionName: String,
                                         secretBallot: Boolean) {
        insertEvent(initiator, "SetElectionSecretBallot", Event.SetElectionSecretBallot(electionName, secretBallot))
    }

    override fun setElectionStatus(initiator: Initiator,
                                   electionName: String,
                                   status: DbStatus) {
        insertEvent(initiator, "SetElectionStatus", Event.SetElectionStatus(electionName, status))
    }

    override fun setCandidates(initiator: Initiator,
                               electionName: String,
                               candidateNames: List<String>) {
        insertEvent(initiator, "SetCandidates", Event.SetCandidates(electionName, candidateNames))
    }

    override fun setVoters(initiator: Initiator,
                           electionName: String,
                           voterNames: List<String>) {
        insertEvent(initiator, "SetVoters", Event.SetVoters(electionName, voterNames))
    }

    override fun setVotersToAll(initiator: Initiator,
                                electionName: String) {
        insertEvent(initiator, "SetVotersToAll", Event.SetVotersToAll(electionName))
    }

    override fun createBallot(initiator: Initiator,
                              electionName: String,
                              userName: String,
                              confirmation: String,
                              whenCast: Instant,
                              rankings: Map<String, Int>) {
        insertEvent(initiator, "CreateBallot", Event.CreateBallot(electionName, userName, confirmation, whenCast, rankings))
    }

    override fun updateBallot(initiator: Initiator,
                              electionName: String,
                              userName: String,
                              whenCast: Instant,
                              rankings: Map<String, Int>) {
        insertEvent(initiator, "UpdateBallot", Event.UpdateBallot(electionName, userName, whenCast, rankings))
    }

    override fun setTally(initiator: Initiator,
                          electionName: String,
                          report: String) {
        insertEvent(initiator, "SetReport", Event.SetReport(electionName, report))
    }

    private fun insertEvent(initiator: Initiator, type: String, event: Event) {
        val json = compact.writeValueAsString(event)
        dbFromResource.update(
                "insert-event.sql",
                clock.instant(),
                initiator.user,
                type,
                json)
    }
}