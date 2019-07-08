package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.domain.db.event.Event
import java.time.Instant

class DbApiCommandsWithEvents(private val fireEvent: (Event) -> Unit) : DbApiCommands {
    override fun createUser(name: String, email: String, salt: String, hash: String) {
        fireEvent(Event.CreateUser(name, email, salt, hash))
    }

    override fun createElection(ownerUserName: String, electionName: String) {
        fireEvent(Event.CreateElection(ownerUserName, electionName))
    }

    override fun setElectionEndDate(electionName: String, end: Instant?) {
        fireEvent(Event.SetElectionEndDate(electionName, end))
    }

    override fun setElectionSecretBallot(electionName: String, secretBallot: Boolean) {
        fireEvent(Event.SetElectionSecretBallot(electionName, secretBallot))
    }

    override fun setElectionStatus(electionName: String, status: DbStatus) {
        fireEvent(Event.SetElectionStatus(electionName, status))
    }

    override fun setCandidates(electionName: String, candidateNames: List<String>) {
        fireEvent(Event.SetCandidates(electionName, candidateNames))
    }

    override fun setVoters(electionName: String, voterNames: List<String>) {
        fireEvent(Event.SetVoters(electionName, voterNames))
    }

    override fun setVotersToAll(electionName: String) {
        fireEvent(Event.SetVotersToAll(electionName))
    }

    override fun createBallot(electionName: String,
                              userName: String,
                              confirmation: String,
                              whenCast: Instant,
                              rankings: Map<String, Int>) {
        fireEvent(Event.CreateBallot(electionName, userName, confirmation, whenCast, rankings))
    }

    override fun updateBallot(electionName: String,
                              userName: String,
                              whenCast: Instant,
                              rankings: Map<String, Int>) {
        fireEvent(Event.UpdateBallot(electionName, userName, whenCast, rankings))
    }

    override fun setTally(electionName: String, report: String) {
        fireEvent(Event.SetReport(electionName, report))
    }
}