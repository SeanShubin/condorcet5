package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.memory.InMemoryTable
import com.seanshubin.condorcet.util.db.memory.Table
import java.time.Instant

class InMemoryDb : DbApi {
    private val userTable: Table<String, DbUser> = InMemoryTable("user")
    private val electionTable: Table<String, DbElection> = InMemoryTable("election")
    private val candidateTable: Table<DbCandidate, DbCandidate> = InMemoryTable("candidate")
    private val voterTable: Table<DbVoter, DbVoter> = InMemoryTable("voter")
    private val ballotTable: Table<DbVoter, DbBallot> = InMemoryTable("ballotTable")
    private val rankingTable: Table<DbUserElectionCandidate, DbRanking> = InMemoryTable("ranking")
    private val tallyTable: Table<DbElectionCandidate, DbTally> = InMemoryTable("tally")

    override fun findUserByName(user: String): DbUser =
            userTable.find { it.name.equals(user, ignoreCase = true) }

    override fun searchUserByName(user: String): DbUser? =
            userTable.searchOne { it.name.equals(user, ignoreCase = true) }

    override fun searchUserByEmail(email: String): DbUser? =
            userTable.searchOne { it.email.equals(email, ignoreCase = true) }

    override fun createUser(name: String,
                            email: String,
                            salt: String,
                            hash: String) {
        userTable.add(DbUser(name, email, salt, hash))
    }

    override fun searchElectionByName(name: String): DbElection? =
            electionTable.searchOne { it.name.equals(name, ignoreCase = true) }

    override fun createElection(owner: String, name: String) {
        electionTable.add(DbElection(
                owner = owner,
                name = name,
                end = null,
                secret = true,
                status = DbStatus.EDITING))
    }

    override fun setElectionEndDate(electionName: String, endDate: Instant?) {
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

    override fun listCandidateNames(election: String): List<String> =
            candidateTable.listWhere { it.electionName == election }.map { it.name }

    override fun listEligibleVoterNames(election: String): List<String> =
            voterTable.listWhere { it.electionName == election }.map { it.userName }

    override fun findElectionByName(name: String): DbElection =
            electionTable.find(name)

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

    override fun electionHasAllVoters(name: String): Boolean =
            userTable.size() == listEligibleVoterNames(name).size

    override fun searchBallot(election: String, user: String): DbBallot? {
        TODO("not implemented")
    }

    override fun listTally(election: String): List<DbTally> {
        TODO("not implemented")
    }

    override fun createBallot(electionName: String, userName: String, confirmation: String, whenCast: Instant, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun updateBallot(electionName: String, userName: String, whenCast: Instant, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun setTally(electionName: String, report: String) {
        TODO("not implemented")
    }

    override fun findBallot(election: String, user: String): DbBallot {
        TODO("not implemented")
    }

    override fun listRankings(election: String, user: String): List<DbRanking> {
        TODO("not implemented")
    }

    override fun listBallotsForElection(election: String): List<DbBallot> {
        TODO("not implemented")
    }

    override fun listBallotsForVoter(voter: String): List<DbBallot> {
        TODO("not implemented")
    }

    override fun listElections(): List<DbElection> = electionTable.listAll()
}
