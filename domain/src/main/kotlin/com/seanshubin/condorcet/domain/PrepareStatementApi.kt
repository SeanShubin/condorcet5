package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.domain.db.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant

class PrepareStatementApi(private val prepareStatement: (String) -> PreparedStatement,
                          private val loadResource:(String)->String) : DbApi {
    private val sqlLookupElectionId = "(select id from election where name = ?)"
    private val sqlLookupUserId = "(select id from user where name = ?)"
    private val sqlLookupStatusId = "(select id from status where name = ?)"

    override fun findUserByName(user: String): DbUser = queryExactlyOneRowDeprecated(
            ::createUser,
            "select name, email, salt, hash from user where name = ?",
            user)

    override fun searchUserByName(user: String): DbUser? = queryZeroOrOneRow(
            ::createUser,
            "select name, email, salt, hash from user where name = ?",
            user)

    override fun searchUserByEmail(email: String): DbUser? = queryZeroOrOneRow(
            ::createUser,
            "select name, email, salt, hash from user where email = ?",
            email)

    override fun findElectionByName(name: String): DbElection = queryExactlyOneRowDeprecated(
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
            name
    )

    override fun searchElectionByName(name: String): DbElection? = queryZeroOrOneRow(
            ::createElection,
            "select owner_id, name, end, secret, status_id from election where name = ?",
            name)

    override fun listCandidateNames(election: String): List<String> = query(
            ::createCandidate,
            """select name
              |from candidate
              |where election_id = (select id from election where name = ?)
            """.trimMargin(),
            election)

    override fun listVoterNames(election: String): List<String> = query(
            ::createCandidate,
            """select
              |  user.name
              |from voter
              |  inner join user
              |  on voter.user_id = user.id
              |where election_id = (select id from election where name = ?)
            """.trimMargin(),
            election)

    override fun electionHasAllVoters(name: String): Boolean {
        val electionVoterCount = electionVoterCount(name)
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

    override fun setElectionEndDate(electionName: String, endDate: Instant?) {
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

    override fun searchBallot(election: String, user: String): DbBallot? = queryZeroOrOneRow(
            ::createDbBallot,
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
            user, election
    )

    override fun findBallot(election: String, user: String): DbBallot = queryExactlyOneRowDeprecated(
            ::createDbBallot,
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
                |  election_id = $sqlLookupElectionId and 
                |  user_id = $sqlLookupUserId""".trimMargin(),
            election, user
    )

    override fun listTally(election: String): List<DbTally> = query(
            ::createDbTally,
            """select
                |  election.name,
                |  candidate.name,
                |  `rank`
                |from
                |  tally
                |  inner join election
                |  on tally.election_id = election.id
                |  inner join candidate
                |  on tally.candidate_id = candidate.id
                |where
                |  election.name = ?""".trimMargin(),
            election
    )

    override fun createBallot(electionName: String, userName: String, confirmation: String, whenCast: Instant, rankings: Map<String, Int>) {
        createDbBallot(electionName, userName, confirmation, whenCast)
        val ballotId = queryBallotId(electionName, userName)
        fun createRanking(ranking: Pair<String, Int>) {
            val (candidateName, rank) = ranking
            val candidateId = queryCandidateId(electionName, candidateName)
            createRanking(ballotId, candidateId, rank)
        }
        rankings.toList().sortedBy { it.second }.forEach(::createRanking)
    }

    override fun updateBallot(electionName: String, userName: String, whenCast: Instant, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun setTally(electionName: String, rankings: Map<String, Int>) {
        TODO("not implemented")
    }

    override fun setVotersToAll(electionName: String) {
        TODO("not implemented")
    }

    override fun listRankings(election: String, user: String): List<DbRanking> =
            query(::createRanking,
                    """select 
                    |  user.name voter,
                    |  election.name election,
                    |  candidate.name candidate,
                    |  ranking.rank `rank`
                    |from
                    |  ranking
                    |  inner join candidate
                    |  on ranking.candidate_id = candidate.id
                    |  inner join ballot
                    |  on ranking.ballot_id = ballot.id
                    |  inner join user
                    |  on ballot.user_id = user.id
                    |  inner join election
                    |  on ballot.election_id = election.id
                    |where
                    |  election.name = ? and
                    |  user.name = ?
                """.trimMargin(),
                    election, user)

    override fun listBallots(election: String): List<DbBallot> =
            query(::createBallot,
                    """select
                    |  user.name user,
                    |  election.name election,
                    |  confirmation,
                    |  when_cast
                    |from
                    |  ballot
                    |  inner join user
                    |  on ballot.user_id = user.id
                    |  inner join election
                    |  on ballot.election_id = election.id
                    |where
                    |  election.name = ?
                """.trimMargin(),
                    election)

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
        update("insert into ranking (ballot_id, candidate_id, `rank`) values (?, ?, ?)",
                ballotId, candidateId, rank)
    }

    private fun queryBallotId(electionName: String, userName: String): Int = queryInt(
            "select id from ballot where election_id = $sqlLookupElectionId and user_id = $sqlLookupUserId",
            electionName, userName
    )

    private fun createDbBallot(electionName: String, userName: String, confirmation: String, whenCast: Instant) {
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
        val end = resultSet.getTimestamp("end")?.toInstant()
        val secret = resultSet.getBoolean("secret")
        val status = resultSet.getString("status")
        return DbElection(owner, name, end, secret, enumValueOf(status.toUpperCase()))
    }

    private fun createDbBallot(resultSet: ResultSet): DbBallot {
        val user = resultSet.getString("user")
        val election = resultSet.getString("election")
        val confirmation = resultSet.getString("confirmation")
        val whenCast = resultSet.getTimestamp("when_cast").toInstant()
        return DbBallot(user, election, confirmation, whenCast)
    }

    private fun createDbTally(resultSet: ResultSet): DbTally {
        val election = resultSet.getString("election")
        val candidate = resultSet.getString("candidate")
        val rank = resultSet.getInt("rank")
        return DbTally(election, candidate, rank)
    }

    private fun createRanking(resultSet: ResultSet): DbRanking {
        val voter = resultSet.getString("voter")
        val election = resultSet.getString("election")
        val candidate = resultSet.getString("candidate")
        val rank = resultSet.getInt("rank")
        return DbRanking(voter, election, candidate, rank)
    }

    private fun createCandidate(resultSet: ResultSet): String = resultSet.getString("name")

    private fun createBallot(resultSet: ResultSet): DbBallot {
        val user = resultSet.getString("user")
        val election = resultSet.getString("election")
        val confirmation = resultSet.getString("confirmation")
        val whenCast = resultSet.getTimestamp("when_cast")
        return DbBallot(user, election, confirmation, whenCast.toInstant())
    }

    private fun <T> queryExactlyOneRowDeprecated(createFunction: (ResultSet) -> T, sql: String, vararg parameters: Any?): T {
        val resultSet = queryResultSet(sql, *parameters)
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

    private fun <T> queryExactlyOneRow(createFunction: (ResultSet) -> T, sqlResource: String, vararg parameters: Any?): T {
        val sql = loadResource(sqlResource)
        val resultSet = queryResultSet(sql, *parameters)
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
        val statement = prepareStatementWithParameters(sql, parameters)
        return executeUpdate(sql, statement, parameters)
    }

    private fun queryResultSet(sql: String, vararg parameters: Any?): ResultSet {
        val statement = prepareStatementWithParameters(sql, parameters)
        return executeQuery(sql, statement, parameters)
    }

    private fun prepareStatementWithParameters(sql: String, parameters: Array<out Any?>): PreparedStatement {
        val statement = prepareStatement(sql)
        parameters.toList().forEachIndexed { index, any ->
            val position = index + 1
            if (any == null) {
                statement.setObject(position, null)
            } else when (any) {
                is String -> statement.setString(position, any)
                is Boolean -> statement.setBoolean(position, any)
                is Int -> statement.setInt(position, any)
                is Instant -> statement.setTimestamp(position, Timestamp.from(any))
                else -> throw UnsupportedOperationException("Unsupported type ${any.javaClass.simpleName}")
            }
        }
        return statement
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
