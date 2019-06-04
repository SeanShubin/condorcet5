package com.seanshubin.condorcet.memory.api

import com.seanshubin.condorcet.domain.*
import com.seanshubin.condorcet.memory.db.InMemoryTable
import com.seanshubin.condorcet.memory.db.Table

class MemoryApi : Api {
    private val user: Table<String, DbUser> = InMemoryTable("user")
    private val election: Table<String, DbElection> = InMemoryTable("election")
    private val candidate: Table<DbCandidate, DbCandidate> = InMemoryTable("candidate")
    private val voter: Table<DbVoter, DbVoter> = InMemoryTable("voter")
    private val ballot: Table<DbVoter, DbBallot> = InMemoryTable("ballot")
    private val ranking: Table<DbUserElectionCandidate, DbRanking> = InMemoryTable("ranking")
    private val tally: Table<DbElectionCandidate, DbTally> = InMemoryTable("tally")

    override fun login(userNameOrUserEmail: String, userPassword: String): Credentials {
        TODO("not implemented")
    }

    override fun register(userName: String, userEmail: String, userPassword: String): Credentials {
        TODO("not implemented")
    }

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun listElections(credentials: Credentials): List<ElectionSummary> {
        TODO("not implemented")
    }

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun endElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun listCandidates(credentials: Credentials, electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun updateCandidateNames(credentials: Credentials, electionName: String, candidateNames: List<String>): List<String> {
        TODO("not implemented")
    }

    override fun listEligibleVoters(credentials: Credentials, electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun areAllVotersEligible(credentials: Credentials, electionName: String): Boolean {
        TODO("not implemented")
    }

    override fun listAllVoters(credentials: Credentials): List<String> {
        TODO("not implemented")
    }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoterNames: List<String>): List<String> {
        TODO("not implemented")
    }

    override fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun listBallots(credentials: Credentials, voterName: String): List<Ballot> {
        TODO("not implemented")
    }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot {
        TODO("not implemented")
    }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: List<Ranking>): Ballot {
        TODO("not implemented")
    }

    override fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): ElectionDetail {
        TODO("not implemented")
    }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail {
        TODO("not implemented")
    }
}