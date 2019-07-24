package com.seanshubin.condorcet.domain.db

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.json.JsonUtil
import java.sql.ResultSet

class ResourceDbApiQueries(private val dbFromResource: DbFromResource) :
        MutableDbQueries,
        DbFromResource by dbFromResource {
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

    override fun findTally(election: String): DbTally = queryExactlyOneRow(
            ::createDbTally,
            "tally-by-election.sql",
            election
    )

    override fun searchTally(election: String): DbTally? = queryZeroOrOneRow(
            ::createDbTally,
            "tally-by-election.sql",
            election
    )

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

    override fun lastEventSynced(): Int {
        TODO("not implemented")
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
        val reportJson = resultSet.getString("report")
        val report = JsonUtil.compact.readValue<Report>(reportJson)
        return DbTally(election, report)
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
}