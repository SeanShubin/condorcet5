package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.db.TransactionFunction

class ApiWrappedInDbTransaction(private val api: Api,
                                private val t: TransactionFunction) : Api {
    override fun login(userNameOrUserEmail: String, userPassword: String): Credentials = t.inTransaction {
        api.login(userNameOrUserEmail, userPassword)
    }

    override fun register(userName: String, userEmail: String, userPassword: String): Credentials = t.inTransaction {
        api.register(userName, userEmail, userPassword)
    }

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail = t.inTransaction {
        api.createElection(credentials, electionName)
    }

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail = t.inTransaction {
        api.copyElection(credentials, newElectionName, electionToCopyName)
    }

    override fun listElections(credentials: Credentials): List<ElectionSummary> = t.inTransaction {
        api.listElections(credentials)
    }

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail = t.inTransaction {
        api.getElection(credentials, electionName)
    }

    override fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail = t.inTransaction {
        api.doneEditingElection(credentials, electionName)
    }

    override fun endElection(credentials: Credentials, electionName: String): ElectionDetail = t.inTransaction {
        api.endElection(credentials, electionName)
    }

    override fun listCandidates(credentials: Credentials, electionName: String): List<String> = t.inTransaction {
        api.listCandidates(credentials, electionName)
    }

    override fun updateCandidateNames(credentials: Credentials,
                                      electionName: String,
                                      candidateNames: List<String>): ElectionDetail = t.inTransaction {
        api.updateCandidateNames(credentials, electionName, candidateNames)
    }

    override fun listEligibleVoters(credentials: Credentials, electionName: String): List<String> = t.inTransaction {
        api.listEligibleVoters(credentials, electionName)
    }

    override fun areAllVotersEligible(credentials: Credentials, electionName: String): Boolean = t.inTransaction {
        api.areAllVotersEligible(credentials, electionName)
    }

    override fun listAllVoters(credentials: Credentials): List<String> = t.inTransaction {
        api.listAllVoters(credentials)
    }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoterNames: List<String>): List<String> = t.inTransaction {
        api.updateEligibleVoters(credentials, electionName, eligibleVoterNames)
    }

    override fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): List<String> = t.inTransaction {
        api.updateEligibleVotersToAll(credentials, electionName)
    }

    override fun listBallots(credentials: Credentials, voterName: String): List<Ballot> = t.inTransaction {
        api.listBallots(credentials, voterName)
    }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot = t.inTransaction {
        api.getBallot(credentials, electionName, voterName)
    }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: List<Ranking>): Ballot = t.inTransaction {
        api.castBallot(credentials, electionName, voterName, rankings)
    }

    override fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): ElectionDetail = t.inTransaction {
        api.setEndDate(credentials, electionName, isoEndDate)
    }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail = t.inTransaction {
        api.setSecretBallot(credentials, electionName, secretBallot)
    }

    override fun tally(credentials: Credentials, electionName: String): Tally = t.inTransaction {
        api.tally(credentials, electionName)
    }
}
