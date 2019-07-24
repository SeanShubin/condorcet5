package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.domain.db.Event.*

class DbEventHandler(private val dbCommands: MutableDbCommands) : EventHandler {
    override fun handle(initiator: Initiator, event: Event) {
        when (event) {
            is CreateUser -> dbCommands.createUser(initiator, event.name, event.email, event.salt, event.hash)
            is CreateElection -> dbCommands.createElection(initiator, event.user, event.election)
            is SetElectionEndDate -> dbCommands.setElectionEndDate(initiator, event.name, event.end)
            is SetElectionSecretBallot -> dbCommands.setElectionSecretBallot(initiator, event.election, event.secret)
            is SetElectionStatus -> dbCommands.setElectionStatus(initiator, event.election, event.status)
            is SetCandidates -> dbCommands.setCandidates(initiator, event.election, event.candidates)
            is SetVoters -> dbCommands.setVoters(initiator, event.election, event.voters)
            is SetVotersToAll -> dbCommands.setVotersToAll(initiator, event.election)
            is CreateBallot -> dbCommands.createBallot(
                    initiator,
                    event.electionName,
                    event.userName,
                    event.confirmation,
                    event.whenCast,
                    event.rankings)
            is UpdateBallot -> dbCommands.updateBallot(
                    initiator,
                    event.electionName,
                    event.userName,
                    event.whenCast,
                    event.rankings)
            is SetReport -> dbCommands.setReport(initiator, event.electionName, event.toReport())
            else -> throw UnsupportedOperationException("Unsupported event $event")
        }
    }
}