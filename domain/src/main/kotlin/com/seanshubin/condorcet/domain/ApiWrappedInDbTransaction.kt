package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.domain.db.Ballot
import com.seanshubin.condorcet.domain.db.Report
import com.seanshubin.condorcet.util.db.TransactionFunction
import java.time.Instant

class ApiWrappedInDbTransaction(private val api: Api,
                                private val t: TransactionFunction) : Api {
    override fun lastSynced(): Int = api.lastSynced()

    override fun login(nameOrEmail: String, password: String): Credentials = t.inTransaction {
        api.login(nameOrEmail, password)
    }

    override fun register(name: String, email: String, password: String): Credentials = t.inTransaction {
        api.register(name, email, password)
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

    override fun setCandidateNames(credentials: Credentials,
                                   electionName: String,
                                   candidateNames: List<String>): ElectionDetail = t.inTransaction {
        api.setCandidateNames(credentials, electionName, candidateNames)
    }

    override fun setVoters(credentials: Credentials,
                           electionName: String,
                           eligibleVoterNames: List<String>): ElectionDetail = t.inTransaction {
        api.setVoters(credentials, electionName, eligibleVoterNames)
    }

    override fun setVotersToAll(credentials: Credentials, electionName: String): ElectionDetail = t.inTransaction {
        api.setVotersToAll(credentials, electionName)
    }

    override fun listBallots(credentials: Credentials, voterName: String): List<Ballot> = t.inTransaction {
        api.listBallots(credentials, voterName)
    }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot = t.inTransaction {
        api.getBallot(credentials, electionName, voterName)
    }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: Map<String, Int>): Ballot = t.inTransaction {
        api.castBallot(credentials, electionName, voterName, rankings)
    }

    override fun setEndDate(credentials: Credentials, electionName: String, endDate: Instant?): ElectionDetail = t.inTransaction {
        api.setEndDate(credentials, electionName, endDate)
    }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail = t.inTransaction {
        api.setSecretBallot(credentials, electionName, secretBallot)
    }

    override fun tally(credentials: Credentials, electionName: String): Report = t.inTransaction {
        api.tally(credentials, electionName)
    }

}
