package com.seanshubin.condorcet.domain.jdbc

import com.seanshubin.condorcet.domain.db.*
import java.sql.PreparedStatement
import java.sql.ResultSet

class PrepareStatementApi(private val prepareStatement: (String) -> PreparedStatement) : DbApi {
    private val sqlLookupElectionId = "(select id from election where name = ?)"
    private val sqlLookupUserId = "(select id from user where name = ?)"
    private val sqlLookupStatusId = "(select id from status where name = ?)"


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

    override fun findElectionByName(electionName: String): DbElection = queryExactlyOneRow(
            ::createElection,
            """select 
              |  user.name as owner,
              |  election.name,
              |  election.end,
              |  election.secret,
              |  status.name as status
              |from election
              |  inner join user
              |  on election.owner_id = user.id
              |  inner join status
              |  on election.status_id = status.id
              |where
              |  election.name = ?""".trimMargin(),
            electionName
    )

    override fun searchElectionByName(electionName: String): DbElection? = queryZeroOrOneRow(
            ::createElection,
            "select owner_id, name, end, secret, status_id from election where name = ?",
            electionName)

    override fun listCandidateNames(electionName: String): List<String> = query(
            ::createCandidate,
            """select name
              |from candidate
              |where election_id = (select id from election where name = ?)
            """.trimMargin(),
            electionName)

    override fun listVoterNames(electionName: String): List<String> = query(
            ::createCandidate,
            """select
              |  user.name
              |from voter
              |  inner join user
              |  on voter.user_id = user.id
              |where election_id = (select id from election where name = ?)
            """.trimMargin(),
            electionName)

    override fun electionHasAllVoters(electionName: String): Boolean {
        val electionVoterCount = electionVoterCount(electionName)
        val allVoterCount = allVoterCount()
        return electionVoterCount == allVoterCount
    }

    override fun createUser(name: String, email: String, salt: String, hash: String) {
        update("insert into user (name, email, salt, hash) values (?, ?, ?, ?)", name, email, salt, hash)
    }

    override fun createElection(owner: String, name: String) {
        val selectOwnerId = "(select id from user where name = ?)"
        val selectStatusId = "(select id from status where name = ?)"
        update("""insert into election (owner_id, name, end, secret, status_id) 
                 |values ($selectOwnerId, ?, ?, ?, $selectStatusId)""".trimMargin(),
                owner, name, null, false, DbStatus.EDITING.name)
    }

    override fun setElectionEndDate(electionName: String, endDate: String?) {
        TODO("not implemented")
    }

    override fun setElectionSecretBallot(electionName: String, secretBallot: Boolean) {
        TODO("not implemented")
    }

    override fun setElectionStatus(electionName: String, status: DbStatus) {
        update("update election set status_id = $sqlLookupStatusId where name = ?", status.name, electionName)
    }

    override fun setCandidates(electionName: String, candidateNames: List<String>) {
        val electionId = queryInt("select * from election where name = ?", electionName)
        removeCandidatesFromElection(electionId)
        candidateNames.forEach { addCandidateToElection(electionId, it) }
    }

    override fun setVoters(electionName: String, voterNames: List<String>) {
        val electionId = queryInt("select * from election where name = ?", electionName)
        removeVotersFromElection(electionId)
        voterNames.forEach { addVoterToElection(electionId, it) }
    }

    override fun searchBallot(electionName: String, userName: String): DbBallot? = queryZeroOrOneRow(
            ::createBallot,
            """select
                |  user.name as user,
                |  election.name as election,
                |  confirmation,
                |  when_cast
                |from ballot
                |  inner join user
                |  on ballot.user_id = user.id
                |  inner join election
                |  on ballot.election_id = election.id
                |where
                |  user.name = ? and
                |  election.name = ?""".trimMargin(),
            userName, electionName
    )

    override fun findBallot(electionName: String, userName: String): DbBallot {
        TODO("not implemented")
    }

    override fun listTally(electionName: String): List<DbTally> {
        TODO("not implemented")
    }

    override fun createBallot(electionName: String, userName: String, confirmation: String, whenCast: String, rankings: Map<String, Int>) {
        createBallot(electionName, userName, confirmation, whenCast)
        val ballotId = queryBallotId(electionName, userName)
        fun createRanking(ranking: Pair<String, Int>) {
            val (candidateName, rank) = ranking
            val candidateId = queryCandidateId(electionName, candidateName)
            createRanking(ballotId, candidateId, rank)
        }
        rankings.toList().sortedBy { it.second }.forEach(::createRanking)
    }

    override fun updateBallot(electionName: String, userName: String, whenCast: String, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun setTally(electionName: String, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun setVotersToAll(electionName: String) {
        TODO("not implemented")
    }

    private fun queryCandidateId(electionName: String, candidateName: String): Int =
            queryInt(
                    """select
                    |  candidate.id
                    |from
                    |  candidate
                    |  inner join election
                    |  on candidate.election_id = election.id
                    |where
                    |  election.name = ? and
                    |  candidate.name = ?""".trimMargin(),
                    electionName, candidateName
            )

    private fun createRanking(ballotId: Int, candidateId: Int, rank: Int) {
        update("insert into ranking (ballot_id, candidate_id, rank) values (?, ?, ?)",
                ballotId, candidateId, rank)
    }

    private fun queryBallotId(electionName: String, userName: String): Int = queryInt(
            "select id from ballot where election = ? and user = ?",
            electionName, userName
    )


    private fun createBallot(electionName: String, userName: String, confirmation: String, whenCast: String) {
        update(
                "insert into ballot (election_id, user_id, confirmation, when_cast) values ($sqlLookupElectionId, $sqlLookupUserId, ?, ?)",
                electionName, userName, confirmation, whenCast
        )
    }

    private fun removeCandidatesFromElection(electionId: Int) {
        update("delete from candidate where election_id = ?", electionId)
    }

    private fun removeVotersFromElection(electionId: Int) {
        update("delete from voter where election_id = ?", electionId)
    }

    private fun addCandidateToElection(electionId: Int, candidateName: String) {
        update("insert into candidate (election_id, name) values (?, ?)", electionId, candidateName)
    }

    private fun addVoterToElection(electionId: Int, voterName: String) {
        val selectVoterId = "(select id from user where name = ?)"
        update("insert into voter (election_id, user_id) values (?, $selectVoterId)", electionId, voterName)
    }

    private fun electionVoterCount(electionName: String): Int =
            queryInt(
                    "select count(id) from voter where election_id = (select id from election where name = ?)",
                    electionName)

    private fun allVoterCount(): Int = queryInt("select count(id) from user")

    private fun createUser(resultSet: ResultSet): DbUser {
        val name = resultSet.getString("name")
        val email = resultSet.getString("email")
        val salt = resultSet.getString("salt")
        val hash = resultSet.getString("hash")
        return DbUser(name, email, salt, hash)
    }

    private fun createElection(resultSet: ResultSet): DbElection {
        val owner = resultSet.getString("owner")
        val name = resultSet.getString("name")
        val end = resultSet.getString("end")
        val secret = resultSet.getBoolean("secret")
        val status = resultSet.getString("status")
        return DbElection(owner, name, end, secret, enumValueOf(status.toUpperCase()))
    }

    private fun createBallot(resultSet: ResultSet): DbBallot {
        val user = resultSet.getString("user")
        val election = resultSet.getString("election")
        val confirmation = resultSet.getString("confirmation")
        val whenCast = resultSet.getString("when_cast")
        return DbBallot(user, election, confirmation, whenCast)
    }

    private fun createCandidate(resultSet: ResultSet): String = resultSet.getString("name")

    private fun <T> queryExactlyOneRow(createFunction: (ResultSet) -> T, sql: String, vararg paremeters: Any?): T {
        val resultSet = queryResultSet(sql, *paremeters)
        return if (resultSet.next()) {
            val result = createFunction(resultSet)
            if (resultSet.next()) {
                throw RuntimeException("No more than 1 row expected for '$sql'")
            }
            result
        } else {
            throw RuntimeException("Exactly 1 row expected for '$sql', got none")
        }
    }

    private fun <T> queryZeroOrOneRow(createFunction: (ResultSet) -> T, sql: String, vararg parameters: Any?): T? {
        val resultSet = queryResultSet(sql, *parameters)
        return if (resultSet.next()) {
            val result = createFunction(resultSet)
            if (resultSet.next()) {
                throw RuntimeException("No more than 1 row expected for '$sql'")
            }
            result
        } else {
            null
        }
    }

    private fun <T> query(createFunction: (ResultSet) -> T, sql: String, vararg parameters: Any?): List<T> {
        val resultSet = queryResultSet(sql, *parameters)
        val results = mutableListOf<T>()
        while (resultSet.next()) {
            results.add(createFunction(resultSet))
        }
        return results
    }

    private fun queryInt(sql: String, vararg parameters: Any?): Int {
        val resultSet = queryResultSet(sql, *parameters)
        return if (resultSet.next()) {
            val result = resultSet.getInt(1)
            if (resultSet.next()) {
                throw RuntimeException("No more than 1 row expected for '$sql'")
            }
            result
        } else {
            throw RuntimeException("Exactly 1 row expected for '$sql', got none")
        }
    }

    private fun update(sql: String, vararg parameters: Any?): Int {
        val statement = prepareStatement(sql)
        parameters.toList().forEachIndexed { index, any ->
            statement.setObject(index + 1, any)
        }
        return executeUpdate(sql, statement, parameters)
    }

    private fun queryResultSet(sql: String, vararg parameters: Any?): ResultSet {
        val statement = prepareStatement(sql)
        parameters.toList().forEachIndexed { index, any ->
            statement.setObject(index + 1, any)
        }
        return executeQuery(sql, statement, parameters)
    }

    private fun executeQuery(sql: String, statement: PreparedStatement, parameters: Array<out Any?>): ResultSet {
        try {
            return statement.executeQuery()
        } catch (ex: Exception) {
            throw RuntimeException(formatMessage(sql, parameters, ex), ex)
        }
    }

    private fun executeUpdate(sql: String, statement: PreparedStatement, parameters: Array<out Any?>): Int {
        try {
            return statement.executeUpdate()
        } catch (ex: Exception) {
            throw RuntimeException(formatMessage(sql, parameters, ex), ex)
        }
    }

    private fun formatMessage(sql: String, parameters: Array<out Any?>, ex: Exception): String {
        val parametersString = parameters.map(::parameterToString).joinToString(", ")
        return "$sql\n$parametersString\n${ex.message}"
    }

    private fun parameterToString(parameter: Any?): String {
        return if (parameter == null) "<null>"
        else {
            val type = parameter.javaClass.simpleName
            "($type)$parameter"
        }
    }
}
