package com.seanshubin.condorcet.json.api

import arrow.core.Try
import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.domain.Credentials
import com.seanshubin.condorcet.json.JsonMappers

class Processor(private val api: Api) : (Credentials, String, String) -> Try<String> {
    override fun invoke(credentials: Credentials, type: String, json: String): Try<String> =
            Try {
                when (type) {
                    "castBallot" -> {
                        val request = JsonMappers.parser.readValue<CastBallotRequest>(json)
                        val response = api.castBallot(credentials, request.electionName, request.voterName, request.rankings)
                        JsonMappers.compact.writeValueAsString(response)
                    }
                    else -> throw UnsupportedOperationException("Unknown command '$type'")
                }
            }
}

data class CastBallotRequest(val electionName: String, val voterName: String, val rankings: Map<String, Int>)
