package com.seanshubin.condorcet.domain.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.time.Instant

interface Event {
    companion object {
        private val mapper = ObjectMapper().registerModule(KotlinModule())
        fun parse(type: String, json: String): Event =
                when (type) {
                    "CreateUser" -> mapper.readValue<CreateUser>(json)
                    "CreateElection" -> mapper.readValue<CreateElection>(json)
                    "SetElectionEndDate" -> mapper.readValue<SetElectionEndDate>(json)
                    "SetElectionSecretBallot" -> mapper.readValue<SetElectionSecretBallot>(json)
                    "SetElectionStatus" -> mapper.readValue<SetElectionStatus>(json)
                    "SetCandidates" -> mapper.readValue<SetCandidates>(json)
                    "SetVoters" -> mapper.readValue<SetVoters>(json)
                    "SetVotersToAll" -> mapper.readValue<SetVotersToAll>(json)
                    else -> throw RuntimeException("Unknown event type $type")
                }

        fun toTypeAndParsable(event: Event): TypeAndParsable =
                TypeAndParsable(event.javaClass.simpleName, mapper.writeValueAsString(event))
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

    data class SetReport(val electionName: String, val report: String) : Event
}
