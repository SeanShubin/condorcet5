package com.seanshubin.condorcet.json.api

import com.seanshubin.condorcet.domain.Credentials
import com.seanshubin.condorcet.domain.ElectionDetail
import com.seanshubin.condorcet.domain.ElectionSummary
import com.seanshubin.condorcet.domain.db.Ballot
import com.seanshubin.condorcet.domain.db.Report

interface Response {
    data class LoginResponse(val credentials: Credentials) : Response
    data class RegisterResponse(val credentials: Credentials) : Response
    data class CreateElectionResponse(val electionDetail: ElectionDetail) : Response
    data class SetEndDateResponse(val electionDetail: ElectionDetail) : Response
    data class SetSecretBallotResponse(val electionDetail: ElectionDetail) : Response
    data class DoneEditingElectionResponse(val electionDetail: ElectionDetail) : Response
    data class EndElectionResponse(val electionDetail: ElectionDetail) : Response
    data class SetCandidateNamesResponse(val electionDetail: ElectionDetail) : Response
    data class SetVotersResponse(val electionDetail: ElectionDetail) : Response
    data class SetVotersToAllResponse(val electionDetail: ElectionDetail) : Response
    data class ListElectionsResponse(val elections: List<ElectionSummary>) : Response
    data class GetElectionResponse(val electionDetail: ElectionDetail) : Response
    data class CopyElectionResponse(val electionDetail: ElectionDetail) : Response
    data class ListBallotsResponse(val ballots: List<Ballot>) : Response
    data class GetBallotResponse(val ballot: Ballot) : Response
    data class CastBallotResponse(val ballot: Ballot) : Response
    data class TallyResponse(val report: Report) : Response
}
