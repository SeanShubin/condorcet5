package com.seanshubin.condorcet.domain

import java.time.Instant

class InputCleaningApi(private val delegate: Api) : Api {
    override fun login(nameOrEmail: String, password: String): Credentials =
            delegate.login(nameOrEmail.clean(), password)

    override fun register(name: String, email: String, password: String): Credentials =
            delegate.register(name.clean(), email.clean(), password)

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail =
            delegate.createElection(credentials, electionName.clean())

    override fun setEndDate(credentials: Credentials, electionName: String, endDate: Instant?): ElectionDetail =
            delegate.setEndDate(credentials, electionName.clean(), endDate)

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail =
            delegate.setSecretBallot(credentials, electionName.clean(), secretBallot)

    override fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail =
            delegate.doneEditingElection(credentials, electionName.clean())

    override fun endElection(credentials: Credentials, electionName: String): ElectionDetail =
            delegate.endElection(credentials, electionName.clean())

    override fun setCandidateNames(credentials: Credentials,
                                   electionName: String,
                                   candidateNames: List<String>): ElectionDetail =
            delegate.setCandidateNames(
                    credentials,
                    electionName.clean(),
                    candidateNames.cleanRemoveDuplicatesIgnoringCase())

    override fun setVoters(credentials: Credentials,
                           electionName: String,
                           eligibleVoterNames: List<String>): ElectionDetail =
            delegate.setVoters(
                    credentials,
                    electionName.clean(),
                    eligibleVoterNames.cleanRemoveDuplicatesIgnoringCase()
            )

    override fun setVotersToAll(credentials: Credentials, electionName: String): ElectionDetail =
            delegate.setVotersToAll(credentials, electionName.clean())

    override fun listElections(credentials: Credentials): List<ElectionSummary> =
            delegate.listElections(credentials)

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail =
            delegate.getElection(credentials, electionName.clean())

    override fun copyElection(credentials: Credentials,
                              newElectionName: String,
                              electionToCopyName: String): ElectionDetail =
            delegate.copyElection(credentials, newElectionName.clean(), electionToCopyName.clean())

    override fun listBallots(credentials: Credentials, voterName: String): List<Ballot> =
            delegate.listBallots(credentials, voterName.clean())

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot =
            delegate.getBallot(credentials, electionName.clean(), voterName.clean())

    override fun castBallot(credentials: Credentials,
                            electionName: String,
                            voterName: String,
                            rankings: Map<String, Int>): Ballot =
            delegate.castBallot(credentials, electionName.clean(), voterName.clean(), rankings)

    override fun tally(credentials: Credentials, electionName: String): Tally =
            delegate.tally(credentials, electionName.clean())

    private fun String.clean() = trim().replace(whitespaceBlock, " ")

    private fun List<String>.cleanRemoveDuplicatesIgnoringCase() = map { it.clean() }.distinctBy { it.toLowerCase() }

    companion object {
        private val whitespaceBlock = Regex("""\s+""")
    }
}