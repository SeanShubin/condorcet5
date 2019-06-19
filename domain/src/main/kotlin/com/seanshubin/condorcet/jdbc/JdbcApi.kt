package com.seanshubin.condorcet.jdbc

import com.seanshubin.condorcet.crypto.PasswordUtil
import com.seanshubin.condorcet.crypto.SaltAndHash
import com.seanshubin.condorcet.domain.*

class JdbcApi(private val passwordUtil: PasswordUtil,
              private val dbExec: DbExec) : Api {
    override fun login(nameOrEmail: String, password: String): Credentials {
        val sql = "select name, salt, hash from user where name = ? or email = ?"
        val resultSet = dbExec.query(sql, nameOrEmail, nameOrEmail)
        if (resultSet.next()) {
            val name = resultSet.getString("name")
            val salt = resultSet.getString("salt")
            val hash = resultSet.getString("hash")
            if (resultSet.next()) {
                throw RuntimeException("more than one entry matching '$nameOrEmail' found")
            }
            val saltAndHash = SaltAndHash(salt, hash)
            return if (passwordUtil.validatePassword(password, saltAndHash)) {
                Credentials(name, password)
            } else {
                throw RuntimeException("invalid password for '$nameOrEmail'")
            }
        } else {
            throw RuntimeException("user or email '$nameOrEmail' not found")
        }
    }

    override fun register(name: String, email: String, password: String): Credentials {
        val sql = "select name, email from user where name = ? or email = ?"
        val resultSet = dbExec.query(sql, name, email)
        if (resultSet.next()) {
            val existingName = resultSet.getString("name")
            val existingEmail = resultSet.getString("email")
            if (name == existingName) {
                throw RuntimeException("user named '$name' already exists")
            }
            if (email == existingEmail) {
                throw RuntimeException("user with email '$email' already exists")
            }
            throw RuntimeException("user '$name' or email '$email' not found")
        } else {
            val sql = "insert into user (name, email, salt, hash) values (?, ?, ?, ?)"
            val (salt, hash) = passwordUtil.createSaltAndHash(password)
            dbExec.update(sql, name, email, salt, hash)
            return Credentials(name, password)
        }
    }

    override fun createElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): ElectionDetail {
        TODO("not implemented")
    }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): ElectionDetail {
        TODO("not implemented")
    }

    override fun doneEditingElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun endElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun updateCandidateNames(credentials: Credentials, electionName: String, candidateNames: List<String>): ElectionDetail {
        TODO("not implemented")
    }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoterNames: List<String>): ElectionDetail {
        TODO("not implemented")
    }

    override fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun listElections(credentials: Credentials): List<ElectionSummary> {
        TODO("not implemented")
    }

    override fun getElection(credentials: Credentials, electionName: String): ElectionDetail {
        TODO("not implemented")
    }

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): ElectionDetail {
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

    override fun tally(credentials: Credentials, electionName: String): Tally {
        TODO("not implemented")
    }
}
