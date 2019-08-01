package com.seanshubin.condorcet.json.api

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.domain.Credentials
import com.seanshubin.condorcet.json.JsonUtil
import com.seanshubin.condorcet.json.api.Response.*
import java.time.Instant

interface Request {
    fun exec(api: Api): Response

    companion object {
        fun parse(name: String, json: String): Request =
                when (name) {
                    "login" -> JsonUtil.parser.readValue<LoginRequest>(json)
                    "register" -> JsonUtil.parser.readValue<RegisterRequest>(json)
                    "createElection" -> JsonUtil.parser.readValue<CreateElectionRequest>(json)
                    "setEndDate" -> JsonUtil.parser.readValue<SetEndDateRequest>(json)
                    "setSecretBallot" -> JsonUtil.parser.readValue<SetSecretBallotRequest>(json)
                    "doneEditingElection" -> JsonUtil.parser.readValue<DoneEditingElectionRequest>(json)
                    "endElection" -> JsonUtil.parser.readValue<EndElectionRequest>(json)
                    "setCandidateNames" -> JsonUtil.parser.readValue<SetCandidateNamesRequest>(json)
                    "setVoters" -> JsonUtil.parser.readValue<SetVotersRequest>(json)
                    "setVotersToAll" -> JsonUtil.parser.readValue<SetVotersToAllRequest>(json)
                    "listElections" -> JsonUtil.parser.readValue<ListElectionsRequest>(json)
                    "getElection" -> JsonUtil.parser.readValue<GetElectionRequest>(json)
                    "copyElection" -> JsonUtil.parser.readValue<CopyElectionRequest>(json)
                    "listBallots" -> JsonUtil.parser.readValue<ListBallotsRequest>(json)
                    "getBallot" -> JsonUtil.parser.readValue<GetBallotRequest>(json)
                    "castBallot" -> JsonUtil.parser.readValue<CastBallotRequest>(json)
                    "tally" -> JsonUtil.parser.readValue<TallyRequest>(json)
                    else -> throw UnsupportedOperationException("Unknown command '$name'")
                }
    }

    data class LoginRequest(val nameOrEmail: String, val password: String) : Request {
        override fun exec(api: Api): Response = LoginResponse(api.login(nameOrEmail, password))
    }

    data class RegisterRequest(val name: String, val email: String, val password: String) : Request {
        override fun exec(api: Api): Response = RegisterResponse(api.register(name, email, password))
    }

    data class CreateElectionRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun exec(api: Api): Response = CreateElectionResponse(api.createElection(credentials, electionName))
    }

    data class SetEndDateRequest(val credentials: Credentials, val electionName: String, val endDate: Instant?) : Request {
        override fun exec(api: Api): Response = SetEndDateResponse(api.setEndDate(credentials, electionName, endDate))
    }

    data class SetSecretBallotRequest(val credentials: Credentials, val electionName: String, val secretBallot: Boolean) : Request {
        override fun exec(api: Api): Response = SetSecretBallotResponse(api.setSecretBallot(credentials, electionName, secretBallot))
    }

    data class DoneEditingElectionRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun exec(api: Api): Response = DoneEditingElectionResponse(api.doneEditingElection(credentials, electionName))
    }

    data class EndElectionRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun exec(api: Api): Response = EndElectionResponse(api.endElection(credentials, electionName))
    }

    data class SetCandidateNamesRequest(val credentials: Credentials, val electionName: String, val candidateNames: List<String>) : Request {
        override fun exec(api: Api): Response = SetCandidateNamesResponse(api.setCandidateNames(credentials, electionName, candidateNames))
    }

    data class SetVotersRequest(val credentials: Credentials, val electionName: String, val eligibleVoterNames: List<String>) : Request {
        override fun exec(api: Api): Response = SetVotersResponse(api.setVoters(credentials, electionName, eligibleVoterNames))
    }

    data class SetVotersToAllRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun exec(api: Api): Response = SetVotersToAllResponse(api.setVotersToAll(credentials, electionName))
    }

    data class ListElectionsRequest(val credentials: Credentials) : Request {
        override fun exec(api: Api): Response = ListElectionsResponse(api.listElections(credentials))
    }

    data class GetElectionRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun exec(api: Api): Response = GetElectionResponse(api.getElection(credentials, electionName))
    }

    data class CopyElectionRequest(val credentials: Credentials, val newElectionName: String, val electionToCopyName: String) : Request {
        override fun exec(api: Api): Response = CopyElectionResponse(api.copyElection(credentials, newElectionName, electionToCopyName))
    }

    data class ListBallotsRequest(val credentials: Credentials, val voterName: String) : Request {
        override fun exec(api: Api): Response = ListBallotsResponse(api.listBallots(credentials, voterName))
    }

    data class GetBallotRequest(val credentials: Credentials, val electionName: String, val voterName: String) : Request {
        override fun exec(api: Api): Response = GetBallotResponse(api.getBallot(credentials, electionName, voterName))
    }

    data class CastBallotRequest(val credentials: Credentials, val electionName: String, val voterName: String, val rankings: Map<String, Int>) : Request {
        override fun exec(api: Api): Response = CastBallotResponse(api.castBallot(credentials, electionName, voterName, rankings))
    }

    data class TallyRequest(val credentials: Credentials, val electionName: String) : Request {
        override fun exec(api: Api): Response = TallyResponse(api.tally(credentials, electionName))
    }

}
