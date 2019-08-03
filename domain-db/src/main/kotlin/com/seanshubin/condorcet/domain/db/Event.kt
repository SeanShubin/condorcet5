package com.seanshubin.condorcet.domain.db

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.json.JsonMappers.compact
import com.seanshubin.condorcet.json.JsonMappers.parser
import java.time.Instant

interface Event {
    companion object {
        fun parse(type: String, json: String): Event =
                when (type) {
                    "CreateUser" -> parser.readValue<CreateUser>(json)
                    "CreateElection" -> parser.readValue<CreateElection>(json)
                    "SetElectionEndDate" -> parser.readValue<SetElectionEndDate>(json)
                    "SetElectionSecretBallot" -> parser.readValue<SetElectionSecretBallot>(json)
                    "SetElectionStatus" -> parser.readValue<SetElectionStatus>(json)
                    "SetCandidates" -> parser.readValue<SetCandidates>(json)
                    "SetVoters" -> parser.readValue<SetVoters>(json)
                    "SetVotersToAll" -> parser.readValue<SetVotersToAll>(json)
                    "CreateBallot" -> parser.readValue<CreateBallot>(json)
                    "SetReport" -> parser.readValue<SetReport>(json)
                    "UpdateBallot" -> parser.readValue<UpdateBallot>(json)
                    else -> throw UnsupportedOperationException("Unsupported event type '$type'")
                }

        fun toTypeAndParsable(event: Event): TypeAndParsable =
                TypeAndParsable(event.javaClass.simpleName, compact.writeValueAsString(event))
    }

    data class TypeAndParsable(val type: String, val parsable: String)

    data class CreateUser(
            val name: String,
            val email: String,
            val salt: String,
            val hash: String) : Event

    data class CreateElection(val user: String,
                              val election: String) : Event

    data class SetElectionEndDate(val name: String,
                                  val end: Instant?) : Event

    data class SetElectionSecretBallot(val election: String,
                                       val secret: Boolean) : Event

    data class SetElectionStatus(val election: String,
                                 val status: DbStatus) : Event

    data class SetCandidates(val election: String,
                             val candidates: List<String>) : Event

    data class SetVoters(val election: String,
                         val voters: List<String>) : Event

    data class SetVotersToAll(val election: String) : Event


    data class CreateBallot(val electionName: String,
                            val userName: String,
                            val confirmation: String,
                            val whenCast: Instant,
                            val rankings: Map<String, Int>) : Event

    data class UpdateBallot(val electionName: String,
                            val userName: String,
                            val whenCast: Instant,
                            val rankings: Map<String, Int>) : Event

    data class SetReport(val electionName: String,
                         val electionOwner: String,
                         val candidates: List<String>,
                         val voted: List<String>,
                         val didNotVote: List<String>,
                         val ballots: List<Ballot>,
                         val preferences: List<List<Int>>,
                         val strongestPaths: List<List<Int>>,
                         val places: List<Place>) : Event {
        constructor(report: Report) : this(
                report.electionName,
                report.electionOwner,
                report.candidates,
                report.voted,
                report.didNotVote,
                report.ballots,
                report.preferences,
                report.strongestPaths,
                report.places)

        fun toReport(): Report = Report(
                electionName,
                electionOwner,
                candidates, voted,
                didNotVote,
                ballots,
                preferences,
                strongestPaths,
                places)
    }
}
