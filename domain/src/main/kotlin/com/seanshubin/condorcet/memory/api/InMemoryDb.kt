package com.seanshubin.condorcet.memory.api

import com.seanshubin.condorcet.db.*
import com.seanshubin.condorcet.memory.db.InMemoryTable
import com.seanshubin.condorcet.memory.db.Table

class InMemoryDb : DbApi {
    private val userTable: Table<String, DbUser> = InMemoryTable("user")
    private val electionTable: Table<String, DbElection> = InMemoryTable("election")
    private val candidateTable: Table<DbCandidate, DbCandidate> = InMemoryTable("candidate")
    private val voterTable: Table<DbVoter, DbVoter> = InMemoryTable("voter")
    private val ballotTable: Table<DbVoter, DbBallot> = InMemoryTable("ballotTable")
    private val rankingTable: Table<DbUserElectionCandidate, DbRanking> = InMemoryTable("ranking")
    private val tallyTable: Table<DbElectionCandidate, DbTally> = InMemoryTable("tally")

    override fun searchUserByName(userName: String): DbUser? =
            userTable.searchOne { it.name.equals(userName, ignoreCase = true) }

    override fun searchUserByEmail(userEmail: String): DbUser? =
            userTable.searchOne { it.email.equals(userEmail, ignoreCase = true) }

    override fun createUser(userName: String, userEmail: String, userPassword: String): DbUser {
        userTable.add(DbUser(userName, userEmail, userPassword))
        return userTable.find(userName)
    }

    override fun searchElectionByName(electionName: String): DbElection? =
            electionTable.searchOne { it.name.equals(electionName, ignoreCase = true) }

    override fun createElection(userName: String, electionName: String): DbElection {
        electionTable.add(DbElection(
                owner = userName,
                name = electionName,
                end = null,
                secret = true,
                status = DbStatus.EDITING))
        return electionTable.find(electionName)
    }

    override fun updateElection(election: DbElection): DbElection {
        electionTable.update(election)
        return election
    }

    override fun listCandidateNames(electionName: String): List<String> =
            candidateTable.listWhere { it.electionName == electionName }.map { it.name }

    override fun listVoterNames(electionName: String): List<String> =
            voterTable.listWhere { it.electionName == electionName }.map { it.userName }

    override fun findElectionByName(electionName: String): DbElection =
            electionTable.find(electionName)

    override fun setCandidates(electionName: String, candidateNames: List<String>) {
        candidateTable.removeWhere { it.electionName == electionName }
        candidateTable.addAll(candidateNames.map { DbCandidate(it, electionName) })
    }

    override fun <T> inTransaction(f: () -> T): T {
        throw UnsupportedOperationException("The in memory database does not support transactions")
    }
}