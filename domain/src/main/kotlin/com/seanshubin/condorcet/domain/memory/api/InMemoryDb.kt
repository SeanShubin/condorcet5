package com.seanshubin.condorcet.domain.memory.api

import com.seanshubin.condorcet.domain.db.*
import com.seanshubin.condorcet.domain.memory.db.InMemoryTable
import com.seanshubin.condorcet.domain.memory.db.Table

class InMemoryDb : DbApi {
    private val userTable: Table<String, DbUser> = InMemoryTable("user")
    private val electionTable: Table<String, DbElection> = InMemoryTable("election")
    private val candidateTable: Table<DbCandidate, DbCandidate> = InMemoryTable("candidate")
    private val voterTable: Table<DbVoter, DbVoter> = InMemoryTable("voter")
    private val ballotTable: Table<DbVoter, DbBallot> = InMemoryTable("ballotTable")
    private val rankingTable: Table<DbUserElectionCandidate, DbRanking> = InMemoryTable("ranking")
    private val tallyTable: Table<DbElectionCandidate, DbTally> = InMemoryTable("tally")

    override fun findUserByName(userName: String): DbUser =
            userTable.find { it.name.equals(userName, ignoreCase = true) }

    override fun searchUserByName(userName: String): DbUser? =
            userTable.searchOne { it.name.equals(userName, ignoreCase = true) }

    override fun searchUserByEmail(userEmail: String): DbUser? =
            userTable.searchOne { it.email.equals(userEmail, ignoreCase = true) }

    override fun createUser(name: String,
                            email: String,
                            salt: String,
                            hash: String) {
        userTable.add(DbUser(name, email, salt, hash))
    }

    override fun searchElectionByName(electionName: String): DbElection? =
            electionTable.searchOne { it.name.equals(electionName, ignoreCase = true) }

    override fun createElection(owner: String, name: String) {
        electionTable.add(DbElection(
                owner = owner,
                name = name,
                end = null,
                secret = true,
                status = DbStatus.EDITING))
    }

    override fun setElectionEndDate(electionName: String, endDate: String?) {
        val oldElection = electionTable.find(electionName)
        val newElection = oldElection.copy(end = endDate)
        electionTable.update(newElection)
    }

    override fun setElectionSecretBallot(electionName: String, secretBallot: Boolean) {
        val oldElection = electionTable.find(electionName)
        val newElection = oldElection.copy(secret = secretBallot)
        electionTable.update(newElection)
    }

    override fun setElectionStatus(electionName: String, status: DbStatus) {
        val oldElection = electionTable.find(electionName)
        val newElection = oldElection.copy(status = status)
        electionTable.update(newElection)
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

    override fun setVoters(electionName: String, voterNames: List<String>) {
        voterTable.removeWhere { it.electionName == electionName }
        voterTable.addAll(voterNames.map { DbVoter(it, electionName) })
    }

    override fun setVotersToAll(electionName: String) {
        voterTable.removeWhere { it.electionName == electionName }
        voterTable.addAll(userTable.listAll().map { DbVoter(it.name, electionName) })
    }

    override fun electionHasAllVoters(electionName: String): Boolean =
            userTable.size() == listVoterNames(electionName).size

    override fun searchBallot(electionName: String, userName: String): DbBallot? {
        TODO("not implemented")
    }

    override fun listTally(electionName: String): List<DbTally> {
        TODO("not implemented")
    }

    override fun createBallot(electionName: String, userName: String, confirmation: String, whenCast: String, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun updateBallot(electionName: String, userName: String, whenCast: String, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun setTally(electionName: String, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun findBallot(electionName: String, userName: String): DbBallot {
        TODO("not implemented")
    }
}