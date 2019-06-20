package com.seanshubin.condorcet.jdbc

import com.seanshubin.condorcet.db.DbApi
import com.seanshubin.condorcet.db.DbElection
import com.seanshubin.condorcet.db.DbStatus
import com.seanshubin.condorcet.db.DbUser
import java.sql.ResultSet

class JdbcApi : DbApi {
    override fun findUserByName(userName: String): DbUser = queryExactlyOneRow(
            ::createUser,
            "select name, email, salt, hash from user where name = ?",
            userName)

    override fun searchUserByName(userName: String): DbUser? = queryZeroOrOneRow(
            ::createUser,
            "select name, email, salt, hash from user where name = ?",
            userName)

    override fun searchUserByEmail(userEmail: String): DbUser? = queryZeroOrOneRow(
            ::createUser,
            "select name, email, salt, hash from user where email = ?",
            userEmail)

    override fun findElectionByName(electionName: String): DbElection {
        TODO("not implemented")
    }

    override fun searchElectionByName(electionName: String): DbElection? {
        TODO("not implemented")
    }

    override fun listCandidateNames(electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun listVoterNames(electionName: String): List<String> {
        TODO("not implemented")
    }

    override fun electionHasAllVoters(electionName: String): Boolean {
        TODO("not implemented")
    }

    override fun createUser(userName: String, userEmail: String, userSalt: String, userHash: String) {
        TODO("not implemented")
    }

    override fun createElection(userName: String, electionName: String): DbElection {
        TODO("not implemented")
    }

    override fun setElectionEndDate(electionName: String, endDate: String?) {
        TODO("not implemented")
    }

    override fun setElectionSecretBallot(electionName: String, secretBallot: Boolean) {
        TODO("not implemented")
    }

    override fun setElectionStatus(electionName: String, status: DbStatus) {
        TODO("not implemented")
    }

    override fun setCandidates(electionName: String, candidateNames: List<String>) {
        TODO("not implemented")
    }

    override fun setVoters(electionName: String, voterNames: List<String>) {
        TODO("not implemented")
    }

    override fun setVotersToAll(electionName: String) {
        TODO("not implemented")
    }

    override fun <T> inTransaction(f: () -> T): T {
        TODO("not implemented")
    }

    private fun createUser(resultSet: ResultSet): DbUser {
        val name = resultSet.getString("name")
        val email = resultSet.getString("email")
        val salt = resultSet.getString("salt")
        val hash = resultSet.getString("hash")
        return DbUser(name, email, salt, hash)
    }

    private fun <T> queryExactlyOneRow(createFunction: (ResultSet) -> T, sql: String, vararg paremeters: Any?): T {
        TODO("not implemented")
    }

    private fun <T> queryZeroOrOneRow(createFunction: (ResultSet) -> T, sql: String, vararg paremeters: Any?): T? {
        TODO("not implemented")
    }
}
