package com.seanshubin.condorcet.json.api

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.domain.Credentials
import com.seanshubin.condorcet.json.JsonMappers
import java.time.Instant

interface Request : (Api) -> Any {
    companion object {
        fun parse(name: String, json: String): Request =
                when (name) {
                    "login" -> parse<LoginRequest>(json)
                    "register" -> parse<RegisterRequest>(json)
                    "createElection" -> parse<CreateElectionRequest>(json)
                    "setEndDate" -> parse<SetEndDateRequest>(json)
                    "setSecretBallot" -> parse<SetSecretBallotRequest>(json)
                    "doneEditingElection" -> parse<DoneEditingElectionRequest>(json)
                    "endElection" -> parse<EndElectionRequest>(json)
                    "setCandidateNames" -> parse<SetCandidateNamesRequest>(json)
                    "setVoters" -> parse<SetVotersRequest>(json)
                    "setVotersToAll" -> parse<SetVotersToAllRequest>(json)
                    "listElections" -> parse<ListElectionsRequest>(json)
                    "getElection" -> parse<GetElectionRequest>(json)
                    "copyElection" -> parse<CopyElectionRequest>(json)
                    "listBallots" -> parse<ListBallotsRequest>(json)
                    "getBallot" -> parse<GetBallotRequest>(json)
                    "castBallot" -> parse<CastBallotRequest>(json)
                    "tally" -> parse<TallyRequest>(json)
                    else -> throw UnsupportedOperationException("Unknown command '$name'")
                }

        private inline fun <reified T> parse(json: String): T = JsonMappers.parser.readValue(json)
    }

    data class LoginRequest(val nameOrEmail: String, val password: String) : Request {
        override fun invoke(api: Api): Any = api.login(nameOrEmail, password)
    }

    data class RegisterRequest(val name: String, val email: String, val password: String) : Request {
        override fun invoke(api: Api): Any = api.register(name, email, password)
    }

    data class CreateElectionRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun invoke(api: Api): Any = api.createElection(credentials, electionName)
    }

    data class SetEndDateRequest(val credentials: Credentials, val electionName: String, val endDate: Instant?) : Request {
        override fun invoke(api: Api): Any = api.setEndDate(credentials, electionName, endDate)
    }

    data class SetSecretBallotRequest(val credentials: Credentials, val electionName: String, val secretBallot: Boolean) : Request {
        override fun invoke(api: Api): Any = api.setSecretBallot(credentials, electionName, secretBallot)
    }

    data class DoneEditingElectionRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun invoke(api: Api): Any = api.doneEditingElection(credentials, electionName)
    }

    data class EndElectionRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun invoke(api: Api): Any = api.endElection(credentials, electionName)
    }

    data class SetCandidateNamesRequest(val credentials: Credentials, val electionName: String, val candidateNames: List<String>) : Request {
        override fun invoke(api: Api): Any = api.setCandidateNames(credentials, electionName, candidateNames)
    }

    data class SetVotersRequest(val credentials: Credentials, val electionName: String, val eligibleVoterNames: List<String>) : Request {
        override fun invoke(api: Api): Any = api.setVoters(credentials, electionName, eligibleVoterNames)
    }

    data class SetVotersToAllRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun invoke(api: Api): Any = api.setVotersToAll(credentials, electionName)
    }

    data class ListElectionsRequest(val credentials: Credentials) : Request {
        override fun invoke(api: Api): Any = api.listElections(credentials)
    }

    data class GetElectionRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun invoke(api: Api): Any = api.getElection(credentials, electionName)
    }

    data class CopyElectionRequest(val credentials: Credentials, val newElectionName: String, val electionToCopyName: String) : Request {
        override fun invoke(api: Api): Any = api.copyElection(credentials, newElectionName, electionToCopyName)
    }

    data class ListBallotsRequest(val credentials: Credentials, val voterName: String) : Request {
        override fun invoke(api: Api): Any = api.listBallots(credentials, voterName)
    }

    data class GetBallotRequest(val credentials: Credentials, val electionName: String, val voterName: String) : Request {
        override fun invoke(api: Api): Any = api.getBallot(credentials, electionName, voterName)
    }

    data class CastBallotRequest(val credentials: Credentials, val electionName: String, val voterName: String, val rankings: Map<String, Int>) : Request {
        override fun invoke(api: Api): Any = api.castBallot(credentials, electionName, voterName, rankings)
    }

    data class TallyRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun invoke(api: Api): Any = api.tally(credentials, electionName)
    }

}
