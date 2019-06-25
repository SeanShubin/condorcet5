package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.util.db.jdbc.ConnectionDbFunctions
import com.seanshubin.condorcet.util.db.jdbc.LoggingPreparedStatement
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.time.Clock
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

// https://blog.oio.de/2018/11/13/how-to-use-junit-5-methodsource-parameterized-tests-with-kotlin/
class DeepTests {
    @Test
    fun contrastFirstPastThePost() {
        // given
        val tester = Tester()
        tester.dbFunctions.inTransactionAlwaysRollback {
            val api = tester.api
            val voters = listOf(
                    "Alice",
                    "Bob",
                    "Carol",
                    "Dave",
                    "Eve",
                    "Frank",
                    "Grace",
                    "Heidi",
                    "Ivy",
                    "Judy")
            voters.forEach {
                api.register(it, "$it@email.com", "password")
            }
            val aliceCredentials = Credentials("Alice", "password")
            val electionName = "Contrast First Past The Post"
            api.createElection(aliceCredentials, electionName)
            api.updateCandidateNames(aliceCredentials, electionName, listOf("Minor Improvements", "Radical Changes", "Status Quo"))
            api.updateEligibleVoters(aliceCredentials, electionName, voters)
            api.doneEditingElection(aliceCredentials, electionName)

            vote(api, "Alice", electionName, mapOf(Pair("Minor Improvements", 1), Pair("Status Quo", 2), Pair("Radical Changes", 3)))
            vote(api, "Bob", electionName, mapOf(Pair("Minor Improvements", 1), Pair("Status Quo", 2), Pair("Radical Changes", 3)))
            vote(api, "Carol", electionName, mapOf(Pair("Minor Improvements", 1), Pair("Status Quo", 2), Pair("Radical Changes", 3)))
            vote(api, "Dave", electionName, mapOf(Pair("Status Quo", 1), Pair("Minor Improvements", 2), Pair("Radical Changes", 3)))
            vote(api, "Eve", electionName, mapOf(Pair("Status Quo", 1), Pair("Minor Improvements", 2), Pair("Radical Changes", 3)))
            vote(api, "Frank", electionName, mapOf(Pair("Status Quo", 1), Pair("Minor Improvements", 2), Pair("Radical Changes", 3)))
            vote(api, "Grace", electionName, mapOf(Pair("Radical Changes", 1), Pair("Minor Improvements", 2), Pair("Status Quo", 3)))
            vote(api, "Heidi", electionName, mapOf(Pair("Radical Changes", 1), Pair("Minor Improvements", 2), Pair("Status Quo", 3)))
            vote(api, "Ivy", electionName, mapOf(Pair("Radical Changes", 1), Pair("Minor Improvements", 2), Pair("Status Quo", 3)))
            vote(api, "Judy", electionName, mapOf(Pair("Radical Changes", 1), Pair("Minor Improvements", 2), Pair("Status Quo", 3)))
            api.endElection(aliceCredentials, electionName)

            // when
            val tally = api.tally(aliceCredentials, electionName)

            // then
            assertEquals(electionName, tally.electionName)
            assertEquals(3, tally.places.size)
            assertEquals("1st", tally.places[0].name)
            assertEquals(listOf("Minor Improvements"), tally.places[0].candidates)
            assertEquals("2nd", tally.places[1].name)
            assertEquals(listOf("Status Quo"), tally.places[1].candidates)
            assertEquals("3rd", tally.places[2].name)
            assertEquals(listOf("Radical Changes"), tally.places[2].candidates)
        }
    }

    private fun vote(api: Api, userName: String, electionName: String, rankings: Map<String, Int>) {
        val credentials = Credentials(userName, "password")
        api.castBallot(credentials, electionName, userName, rankings)
    }

    class Tester {
        val scheme = "jdbc:mysql"
        val host = "prototype.cmph7klf3qhg.us-west-1.rds.amazonaws.com"
        val database = "prototype"
        val url = "$scheme://$host/$database"
        val user = "prototype"
        val password = "prototype"
        val connection = DriverManager.getConnection(url, user, password)
        val dbFunctions = ConnectionDbFunctions(connection)
        val clock = Clock.systemDefaultZone()
        val oneWayHash: OneWayHash = Sha256Hash()
        val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
        val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        val emitLine: (String) -> Unit = ::println
        fun prepareStatement(sql: String): PreparedStatement =
                LoggingPreparedStatement(sql, connection.prepareStatement(sql), emitLine)

        val db = PrepareStatementApi(::prepareStatement)
        val seed = 12345L
        val random = Random(seed)
        val api = ApiBackedByDb(db, clock, passwordUtil, uniqueIdGenerator, random)
    }
}
