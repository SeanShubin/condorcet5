package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.crypto.Uuid4
import com.seanshubin.condorcet.domain.Credentials
import com.seanshubin.condorcet.domain.ElectionStatus
import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.table.formatter.RowStyleTableFormatter
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ResultSetIterator
import java.nio.file.Paths
import java.time.Clock
import java.time.Instant
import kotlin.test.assertEquals

fun main() {
    val logger = LoggerFactory.instanceDefaultZone.createLogger(Paths.get("out", "log"), "integration-test")
    val emit: (String) -> Unit = logger::log
    fun sqlEvent(sql: String): Unit = emit(sql)
    ConnectionFactory.withConnection(
            Connections.local,
            ::sqlEvent) { connection ->
        fun execQuery(sql: String) {
            connection.execQuery(sql) { resultSet ->
                val iterator = ResultSetIterator.consume(resultSet)
                val header = iterator.columnNames
                val table = iterator.consumeRemainingToTable()
                val formattedTable = RowStyleTableFormatter.boxDrawing.format(listOf(header) + table)
                formattedTable.forEach(emit)
            }
        }

        fun execUpdate(sql: String) {
            connection.execUpdate(sql)
        }
        connection.execUpdate("create database if not exists sample")
        connection.execUpdate("use sample")
        SampleData.dropTables().forEach(::execUpdate)
        SampleData.createTables().forEach(::execUpdate)
        SampleData.staticData().forEach(::execUpdate)
        SampleData.displayGeneric().forEach(::execQuery)
        SampleData.displayDebug().forEach(::execQuery)
        val clock = Clock.systemDefaultZone()
        val uniqueIdGenerator = Uuid4()

        ApiFactory.withApi(connection, clock, uniqueIdGenerator) { api ->

            fun vote(userName: String, electionName: String, rankings: Map<String, Int>) {
                val credentials = Credentials(userName, "password")
                api.castBallot(credentials, electionName, userName, rankings)
            }

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
            api.setCandidateNames(aliceCredentials, electionName, listOf("Minor Improvements", "Radical Changes", "Status Quo"))
            api.setVoters(aliceCredentials, electionName, voters)
            api.doneEditingElection(aliceCredentials, electionName)

            vote("Alice", electionName, mapOf(Pair("Minor Improvements", 1), Pair("Status Quo", 2), Pair("Radical Changes", 3)))
            vote("Bob", electionName, mapOf(Pair("Minor Improvements", 1), Pair("Status Quo", 2), Pair("Radical Changes", 3)))
            vote("Carol", electionName, mapOf(Pair("Minor Improvements", 1), Pair("Status Quo", 2), Pair("Radical Changes", 3)))
            vote("Dave", electionName, mapOf(Pair("Status Quo", 1), Pair("Minor Improvements", 2), Pair("Radical Changes", 3)))
            vote("Eve", electionName, mapOf(Pair("Status Quo", 1), Pair("Minor Improvements", 2), Pair("Radical Changes", 3)))
            vote("Frank", electionName, mapOf(Pair("Status Quo", 1), Pair("Minor Improvements", 2), Pair("Radical Changes", 3)))
            vote("Grace", electionName, mapOf(Pair("Radical Changes", 1), Pair("Minor Improvements", 2), Pair("Status Quo", 3)))
            vote("Heidi", electionName, mapOf(Pair("Radical Changes", 1), Pair("Minor Improvements", 2), Pair("Status Quo", 3)))
            vote("Ivy", electionName, mapOf(Pair("Radical Changes", 1), Pair("Minor Improvements", 2), Pair("Status Quo", 3)))
            vote("Judy", electionName, mapOf(Pair("Radical Changes", 1), Pair("Minor Improvements", 2), Pair("Status Quo", 3)))
            api.endElection(aliceCredentials, electionName)

            val tally = api.tally(aliceCredentials, electionName)
            assertEquals("Contrast First Past The Post", tally.electionName)
            assertEquals(3, tally.places.size)
            assertEquals("1st", tally.places[0].name)
            assertEquals(listOf("Minor Improvements"), tally.places[0].candidates)
            assertEquals("2nd", tally.places[1].name)
            assertEquals(listOf("Status Quo"), tally.places[1].candidates)
            assertEquals("3rd", tally.places[2].name)
            assertEquals(listOf("Radical Changes"), tally.places[2].candidates)

            val elections = api.listElections(aliceCredentials)
            assertEquals(1, elections.size)
            assertEquals("", elections[0].ownerName)
            assertEquals("", elections[0].name)
            assertEquals(Instant.parse(""), elections[0].end)
            assertEquals(false, elections[0].secretBallot)
            assertEquals(ElectionStatus.COMPLETE, elections[0].status)
            assertEquals(3, elections[0].candidateCount)
            assertEquals(10, elections[0].voterCount)

            val ballots = api.listBallots(aliceCredentials, "Alice")
            assertEquals(1, ballots.size)
            assertEquals("", ballots[0].user)
            assertEquals("", ballots[0].election)
            assertEquals("", ballots[0].confirmation)
            assertEquals(Instant.parse(""), ballots[0].whenCast)
            assertEquals(false, ballots[0].active)

            val rankings = ballots[0].rankings
            assertEquals(3, rankings.size)
            assertEquals(1, rankings[0].rank)
            assertEquals("", rankings[0].candidateName)
            assertEquals(1, rankings[1].rank)
            assertEquals("", rankings[1].candidateName)
            assertEquals(1, rankings[2].rank)
            assertEquals("", rankings[2].candidateName)

            val ballot = api.getBallot(aliceCredentials, "Contrast First Past The Post", "Alice")
            assertEquals(ballots[0], ballot)
        }
    }
}