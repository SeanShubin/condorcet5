package com.seanshubin.condorcet.memory.api

import com.seanshubin.condorcet.db.*
import com.seanshubin.condorcet.memory.db.InMemoryTable
import com.seanshubin.condorcet.memory.db.Table

class InMemoryDb : DbApi {
    private val user: Table<String, DbUser> = InMemoryTable("user")
    private val election: Table<String, DbElection> = InMemoryTable("election")
    private val candidate: Table<DbCandidate, DbCandidate> = InMemoryTable("candidate")
    private val voter: Table<DbVoter, DbVoter> = InMemoryTable("voter")
    private val ballot: Table<DbVoter, DbBallot> = InMemoryTable("ballot")
    private val ranking: Table<DbUserElectionCandidate, DbRanking> = InMemoryTable("ranking")
    private val tally: Table<DbElectionCandidate, DbTally> = InMemoryTable("tally")

    override fun searchUserByName(userName: String): DbUser? =
            user.searchOne { it.name.equals(userName, ignoreCase = true) }

    override fun searchUserByEmail(userEmail: String): DbUser? =
            user.searchOne { it.email.equals(userEmail, ignoreCase = true) }

    override fun createUser(userName: String, userEmail: String, userPassword: String): DbUser {
        user.add(DbUser(userName, userEmail, userPassword))
        return user.find(userName)
    }

    override fun searchElectionByName(electionName: String): DbElection? {
        TODO("not implemented")
    }

    override fun createElection(userName: String, electionName: String): DbElection {
        TODO("not implemented")
    }

    override fun listCandidateNames(electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun listVoterNames(electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun <T> inTransaction(f: () -> T): T {
        TODO("not implemented")
    }
}