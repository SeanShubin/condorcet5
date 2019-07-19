package com.seanshubin.condorcet.domain.db

import java.time.Instant

class CompositeDbApiCommands(private vararg val apis: DbApiCommands) : DbApiCommands {
    override fun createUser(initiator: Initiator, name: String, email: String, salt: String, hash: String) {
        apis.forEach { it.createUser(initiator, name, email, salt, hash) }
    }

    override fun createElection(initiator: Initiator, ownerUserName: String, electionName: String) {
        apis.forEach { it.createElection(initiator, ownerUserName, electionName) }
    }

    override fun setElectionEndDate(initiator: Initiator, electionName: String, end: Instant?) {
        apis.forEach { it.setElectionEndDate(initiator, electionName, end) }
    }

    override fun setElectionSecretBallot(initiator: Initiator, electionName: String, secretBallot: Boolean) {
        apis.forEach { it.setElectionSecretBallot(initiator, electionName, secretBallot) }
    }

    override fun setElectionStatus(initiator: Initiator, electionName: String, status: DbStatus) {
        apis.forEach { it.setElectionStatus(initiator, electionName, status) }
    }

    override fun setCandidates(initiator: Initiator, electionName: String, candidateNames: List<String>) {
        apis.forEach { it.setCandidates(initiator, electionName, candidateNames) }
    }

    override fun setVoters(initiator: Initiator, electionName: String, voterNames: List<String>) {
        apis.forEach { it.setVoters(initiator, electionName, voterNames) }
    }

    override fun setVotersToAll(initiator: Initiator, electionName: String) {
        apis.forEach { it.setVotersToAll(initiator, electionName) }
    }

    override fun createBallot(initiator: Initiator, electionName: String, userName: String, confirmation: String, whenCast: Instant, rankings: Map<String, Int>) {
        apis.forEach { it.createBallot(initiator, electionName, userName, confirmation, whenCast, rankings) }
    }

    override fun updateBallot(initiator: Initiator, electionName: String, userName: String, whenCast: Instant, rankings: Map<String, Int>) {
        apis.forEach { it.updateBallot(initiator, electionName, userName, whenCast, rankings) }
    }

    override fun setReport(initiator: Initiator, electionName: String, report: Report) {
        apis.forEach { it.setReport(initiator, electionName, report) }
    }
}
