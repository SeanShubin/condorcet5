package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.domain.db.*
import com.seanshubin.condorcet.util.db.ConnectionWrapper
import java.sql.ResultSet
import java.time.Instant

class PrepareStatementApi(private val connection: ConnectionWrapper,
                          private val loadResource: (String) -> String) : DbApi {
    override fun findUserByName(user: String): DbUser = queryExactlyOneRow(
            ::createUser,
            "user-by-name.sql",
            user)

    override fun searchUserByName(user: String): DbUser? = queryZeroOrOneRow(
            ::createUser,
            "user-by-name.sql",
            user)

    override fun searchUserByEmail(email: String): DbUser? = queryZeroOrOneRow(
            ::createUser,
            "user-by-email.sql",
            email)

    override fun findElectionByName(name: String): DbElection = queryExactlyOneRow(
            ::createElection,
            "election-by-name.sql",
            name
    )

    override fun searchElectionByName(name: String): DbElection? = queryZeroOrOneRow(
            ::createElection,
            "election-by-name.sql",
            name)

    override fun listCandidateNames(election: String): List<String> = query(
            ::createCandidate,
            "candidate-names-by-election.sql",
            election)

    override fun listEligibleVoterNames(election: String): List<String> = query(
            ::createCandidate,
            "voter-names-by-election.sql",
            election)

    override fun electionHasAllVoters(election: String): Boolean {
        val electionVoterCount = queryInt("count-voters-for-election.sql", election)
        val allVoterCount = queryInt("count-users.sql")
        return electionVoterCount == allVoterCount
    }

    override fun createUser(name: String, email: String, salt: String, hash: String) {
        update("create-user.sql", name, email, salt, hash)
    }

    override fun createElection(owner: String, name: String) {
        update("create-election.sql", owner, name, null, false, DbStatus.EDITING.name)
    }

    override fun setElectionEndDate(electionName: String, endDate: Instant?) {
        update("set-election-end-date.sql", endDate, electionName)
    }

    override fun setElectionSecretBallot(electionName: String, secretBallot: Boolean) {
        update("set-election-secret-ballot.sql", secretBallot, electionName)
    }

    override fun setElectionStatus(electionName: String, status: DbStatus) {
        update("set-election-status.sql", status.name, electionName)
    }

    override fun setCandidates(electionName: String, candidateNames: List<String>) {
        update("remove-candidates-from-election.sql", electionName)
        candidateNames.forEach {
            update("add-candidate-to-election.sql", electionName, it)
        }
    }

    override fun setVoters(electionName: String, voterNames: List<String>) {
        update("remove-voters-from-election.sql", electionName)
        voterNames.forEach {
            update("add-voter-to-election.sql", electionName, it)
        }
    }

    override fun searchBallot(election: String, user: String): DbBallot? = queryZeroOrOneRow(
            ::createDbBallot,
            "ballot-by-user-election.sql",
            user, election
    )

    override fun findBallot(election: String, user: String): DbBallot = queryExactlyOneRow(
            ::createDbBallot,
            "ballot-by-user-election.sql",
            user, election
    )

    override fun listTally(election: String): List<DbTally> = query(
            ::createDbTally,
            "tally-by-election.sql",
            election
    )

    override fun createBallot(electionName: String, userName: String, confirmation: String, whenCast: Instant, rankings: Map<String, Int>) {
        createDbBallot(electionName, userName, confirmation, whenCast)
        val ballotId = queryInt("ballot-id-by-user-election.sql", userName, electionName)
        createRankings(ballotId, userName, electionName, rankings)
    }


    override fun updateBallot(electionName: String, userName: String, whenCast: Instant, rankings: Map<String, Int>) {
        val ballotId = queryInt("ballot-id-by-user-election.sql", userName, electionName)
        removeRankings(ballotId)
        createRankings(ballotId, userName, electionName, rankings)
    }

    private fun removeRankings(ballotId: Int) {
        update("remove-rankings-by-ballot.sql", ballotId)
    }

    private fun createRankings(ballotId: Int, userName: String, electionName: String, rankings: Map<String, Int>) {
        fun createRanking(ranking: Pair<String, Int>) {
            val (candidateName, rank) = ranking
            val candidateId = queryInt("candidate-id-by-election-candidate.sql", electionName, candidateName)
            createRanking(ballotId, candidateId, rank)
        }
        rankings.toList().sortedBy { it.second }.forEach(::createRanking)
    }

    override fun setTally(electionName: String, report: String) {
        update("create-tally.sql", electionName, report)
    }

    override fun setVotersToAll(electionName: String) {
        update("set-voters-to-all.sql", electionName)
    }

    override fun listRankings(election: String, user: String): List<DbRanking> =
            query(::createRanking,
                    "ranking-by-user-election.sql",
                    user, election)

    override fun listBallotsForElection(election: String): List<DbBallot> =
            query(::createBallot,
                    "ballot-by-election.sql",
                    election)

    override fun listElections(): List<DbElection> =
            query(::createElection, "list-elections.sql")

    override fun listBallotsForVoter(voter: String): List<DbBallot> =
            query(::createBallot, "ballots-by-user.sql", voter)

    private fun createRanking(ballotId: Int, candidateId: Int, rank: Int) {
        update("create-ranking.sql", ballotId, candidateId, rank)
    }

    private fun createDbBallot(electionName: String, userName: String, confirmation: String, whenCast: Instant) {
        update(
                "create-ballot.sql",
                electionName, userName, confirmation, whenCast
        )
    }

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

    private fun <T> queryExactlyOneRow(createFunction: (ResultSet) -> T,
                                       sqlResource: String,
                                       vararg parameters: Any?): T {
        val sql = loadResource(sqlResource)
        return connection.execQuery(sql, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = createFunction(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$sql'")
                }
                result
            } else {
                throw RuntimeException("Exactly 1 row expected for '$sql', got none")
            }
        }
    }

    private fun <T> queryZeroOrOneRow(createFunction: (ResultSet) -> T,
                                      sqlResource: String,
                                      vararg parameters: Any?): T? {
        val sql = loadResource(sqlResource)
        return connection.execQuery(sql, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = createFunction(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$sql'")
                }
                result
            } else {
                null
            }
        }
    }

    private fun <T> query(createFunction: (ResultSet) -> T,
                          sqlResource: String,
                          vararg parameters: Any?): List<T> {
        val sql = loadResource(sqlResource)
        return connection.execQuery(sql, *parameters) { resultSet ->
            val results = mutableListOf<T>()
            while (resultSet.next()) {
                results.add(createFunction(resultSet))
            }
            results
        }
    }

    private fun queryInt(sqlResource: String, vararg parameters: Any?): Int {
        val sql = loadResource(sqlResource)
        return connection.execQuery(sql, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = resultSet.getInt(1)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$sql'")
                }
                result
            } else {
                throw RuntimeException("Exactly 1 row expected for '$sql', got none")
            }
        }
    }

    private fun update(sqlResource: String, vararg parameters: Any?): Int {
        val sql = loadResource(sqlResource)
        return connection.execUpdate(sql, *parameters)
    }
}
