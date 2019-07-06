package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.domain.Credentials
import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.table.formatter.RowStyleTableFormatter
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ResultSetIterator
import java.nio.file.Paths
import java.time.Clock

fun main() {
    val logger = LoggerFactory.create(Paths.get("out", "log"), "sample-data")
    val emit: (String) -> Unit = logger::log
    fun sqlEvent(sql: String): Unit = emit(sql.trim() + ";")
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

        val clock = Clock.systemDefaultZone()

        ApiFactory.withApi(connection, clock) { api ->
            val alice = Credentials("Alice", "alice-password")
            val bob = Credentials("Bob", "bob-password")
            val carol = Credentials("Carol", "carol-password")
            val dave = Credentials("Dave", "dave-password")
            val favoriteIceCream = "Favorite Ice Cream"
            val government = "Government"
            val dystopia = "Dystopia"
            val pet = "Pet"
            val scienceFiction = "Science Fiction"
            val fantasy = "Fantasy"
            api.register("Alice", "alice@email.com", "alice-password")
            api.register("Bob", "bob@email.com", "bob-password")
            api.register("Carol", "carol@email.com", "carol-password")
            api.register("Dave", "dave@email.com", "dave-password")
            api.register("Eve", "eve@email.com", "eve-password")
            api.register("Frank", "frank@email.com", "frank-password")
            api.register("Grace", "grace@email.com", "grace-password")
            api.register("Heidi", "heidi@email.com", "heidi-password")
            api.register("Ivy", "ivy@email.com", "ivy-password")
            api.register("Judy", "judy@email.com", "judy-password")

            api.createElection(alice, favoriteIceCream)

            val fiveMinutesFromNow = clock.instant().plusSeconds(5)
            api.setEndDate(alice, favoriteIceCream, fiveMinutesFromNow)
            api.setSecretBallot(alice, favoriteIceCream, true)
            api.setCandidateNames(alice, favoriteIceCream, listOf("Chocolate", "Vanilla", "Strawberry"))
            api.setVoters(alice, favoriteIceCream, listOf("Alice", "Bob", "Carol", "Dave", "Eve"))
            api.doneEditingElection(alice, favoriteIceCream)

            api.createElection(alice, government)
            api.setCandidateNames(alice, government, listOf("Monarchy", "Aristocracy", "Democracy"))
            api.setVoters(alice, government, listOf("Bob", "Carol", "Dave", "Eve"))
            api.doneEditingElection(alice, government)
            api.endElection(alice, government)

            api.createElection(alice, dystopia)
            api.setCandidateNames(alice, dystopia, listOf("1984", "Fahrenheit 451", "Brave New World"))
            api.setVotersToAll(alice, dystopia)

            api.createElection(bob, pet)
            api.setCandidateNames(bob, pet, listOf("Cat", "Dog", "Bird", "Fish", "Reptile"))
            api.setVotersToAll(bob, pet)

            api.createElection(carol, scienceFiction)
            api.setCandidateNames(carol, scienceFiction, listOf("Babylon 5", "Star Trek", "Blake's 7", "Firefly"))
            api.setVotersToAll(carol, scienceFiction)

            api.createElection(dave, fantasy)
            api.setCandidateNames(dave, fantasy, listOf("Marvel Cinematic Universe", "Lord of the Rings", "Harry Potter"))
            api.setVotersToAll(dave, fantasy)

            api.copyElection(dave, "Government 2", "Government")
        }

//        SampleData.displayGeneric().forEach(::execQuery)
        SampleData.displayDebug().forEach(::execQuery)
    }
}
/*
        assertEquals(alice, api.login("Alice", "alice-password"))
        assertEquals(bob, api.login("bob@email.com", "bob-password"))
        assertEquals(6, api.listElections(alice).size)
        assertTrue(api.getElection(alice, dystopia).isAllVoters)
        assertEquals(listOf("Bob", "Carol", "Eve", "Dave"), api.getElection(alice, dystopia).voterNames)


    // ballot
    fun listBallots(credentials: Credentials, voterName: String): List<Ballot>
    fun getBallot(credentials: Credentials, electionName: String, voterName: String): Ballot
    fun castBallot(credentials: Credentials, electionName: String, voterName: String, rankings: Map<String, Int>): Ballot

    // tally
    fun tally(credentials: Credentials, electionName: String): Tally
*/
