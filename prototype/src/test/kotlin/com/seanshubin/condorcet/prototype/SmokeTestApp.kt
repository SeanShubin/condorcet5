package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.domain.Credentials
import com.seanshubin.condorcet.domain.db.Ballot
import com.seanshubin.condorcet.domain.db.Place
import com.seanshubin.condorcet.domain.db.Ranking
import com.seanshubin.condorcet.domain.db.Report
import com.seanshubin.condorcet.json.JsonMappers.pretty
import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.table.formatter.RowStyleTableFormatter
import com.seanshubin.condorcet.util.ListDifference
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ResultSetIterator
import java.nio.file.Paths
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals
import kotlin.test.assertTrue

fun main() {
    val loggerFactory = LoggerFactory.instanceDefaultZone
    val logDir = loggerFactory.createLogGroup(Paths.get("out", "log", "sample-data"))
    val sqlLogger = logDir.create("sql")
    fun logSql(sql: String) = sqlLogger.log("${sql.trim()};")
    val connection = ConnectionFactory.createConnection(Connections.local, ::logSql)
    val debugLogger = logDir.create("debug")
    fun execQuery(sql: String) {
        connection.query(sql) { resultSet ->
            val iterator = ResultSetIterator.consume(resultSet)
            val header = iterator.columnNames
            val table = iterator.consumeRemainingToTable()
            val formattedTable = RowStyleTableFormatter.boxDrawing.format(listOf(header) + table)
            debugLogger.log(sql.trim())
            formattedTable.forEach(debugLogger::log)
        }
    }

    fun execUpdate(sql: String) {
        connection.update(sql)
    }
    execUpdate("create database if not exists sample")
    execUpdate("use sample")
    SampleData.dropTables().forEach(::execUpdate)
    SampleData.createTables().forEach(::execUpdate)
    SampleData.staticData().forEach(::execUpdate)

    val clock = MinuteAtATime()
    val uniqueIdGenerator = DeterministicUniqueIdGenerator()
    val api = ApiFactory.createApi(connection, clock, uniqueIdGenerator)

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
    assertEquals(0, api.lastSynced())
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

    val fiveMinutesFromNow = clock.instant().plus(5, ChronoUnit.MINUTES)
    api.setEndDate(alice, favoriteIceCream, fiveMinutesFromNow)
    api.setSecretBallot(alice, favoriteIceCream, true)
    api.setCandidateNames(alice, favoriteIceCream, listOf("Chocolate", "Vanilla", "Strawberry"))
    api.setVoters(alice, favoriteIceCream, listOf("Alice", "Bob", "Carol", "Dave", "Eve"))
    api.doneEditingElection(alice, favoriteIceCream)

    api.createElection(alice, government)
    api.setCandidateNames(alice, government, listOf("Monarchy", "Aristocracy", "Democracy"))
    api.setVoters(alice, government, listOf("Bob", "Carol", "Dave", "Eve"))
    api.doneEditingElection(alice, government)
    api.castBallot(bob, government, "Bob", mapOf(Pair("Monarchy", 1), Pair("Aristocracy", 2), Pair("Democracy", 3)))
    api.castBallot(carol, government, "Carol", mapOf(Pair("Democracy", 1), Pair("Aristocracy", 2), Pair("Monarchy", 3)))
    api.castBallot(dave, government, "Dave", mapOf(Pair("Democracy", 1), Pair("Aristocracy", 2), Pair("Monarchy", 3)))
    api.endElection(alice, government)

    api.createElection(alice, dystopia)
    api.setCandidateNames(alice, dystopia, listOf("1984", "Fahrenheit 451", "Brave New World"))
    api.setVotersToAll(alice, dystopia)
    api.doneEditingElection(alice, dystopia)
    api.castBallot(alice, dystopia, "Alice", mapOf(
            Pair("1984", 1),
            Pair("Brave New World", 2),
            Pair("Fahrenheit 451", 3)))

    api.createElection(bob, pet)
    api.setCandidateNames(bob, pet, listOf("Cat", "Dog", "Bird", "Fish", "Reptile"))
    api.setVotersToAll(bob, pet)
    api.doneEditingElection(bob, pet)
    api.castBallot(alice, pet, "Alice", mapOf(
            Pair("Dog", 1),
            Pair("Cat", 2)))
    api.castBallot(alice, pet, "Alice", mapOf(
            Pair("Cat", 1),
            Pair("Dog", 2)))
    api.castBallot(bob, pet, "Bob", mapOf(
            Pair("Cat", 1),
            Pair("Bird", 2)))
    api.castBallot(carol, pet, "Carol", mapOf(
            Pair("Bird", 1),
            Pair("Cat", 2),
            Pair("Dog", 3)))
    api.castBallot(dave, pet, "Dave", mapOf(
            Pair("Cat", 1),
            Pair("Reptile", 2),
            Pair("Dog", 3),
            Pair("Bird", 4)))
    api.endElection(bob, pet)

    api.createElection(carol, scienceFiction)
    api.setCandidateNames(carol, scienceFiction, listOf("Babylon 5", "Star Trek", "Blake's 7", "Firefly"))
    api.setVotersToAll(carol, scienceFiction)

    api.createElection(dave, fantasy)
    api.setCandidateNames(dave, fantasy, listOf("Marvel Cinematic Universe", "Lord of the Rings", "Harry Potter"))
    api.setVotersToAll(dave, fantasy)

    api.copyElection(dave, "Government Copy", "Government")

    assertEquals(alice, api.login("Alice", "alice-password"))
    assertEquals(bob, api.login("bob@email.com", "bob-password"))
    assertEquals(7, api.listElections(alice).size)
    assertTrue(api.getElection(alice, dystopia).isAllVoters)
    assertEquals(listOf("Alice", "Bob", "Carol", "Dave", "Eve"), api.getElection(alice, favoriteIceCream).voterNames)
    assertEquals(listOf("Dystopia", pet), api.listBallots(alice, "Alice").map { it.election })
    assertEquals(listOf(
            Ranking(1, "Cat"),
            Ranking(2, "Dog"),
            Ranking(null, "Fish"),
            Ranking(null, "Reptile"),
            Ranking(null, "Bird")), api.getBallot(alice, pet, "Alice").rankings)
    val expectedTally = Report(
            electionName = "Pet",
            electionOwner = "Bob",
            candidates = listOf("Bird", "Cat", "Dog", "Fish", "Reptile"),
            voted = listOf("Alice", "Bob", "Carol", "Dave"),
            didNotVote = listOf("Eve", "Frank", "Grace", "Heidi", "Ivy", "Judy"),
            ballots = listOf(
                    Ballot(user = "Alice",
                            election = "Pet",
                            confirmation = "unique-id-15",
                            whenCast = Instant.parse("2019-07-08T22:39:08Z"),
                            active = false,
                            rankings = listOf(Ranking(rank = 1, candidateName = "Cat"),
                                    Ranking(rank = 2, candidateName = "Dog"))),
                    Ballot(user = "Bob",
                            election = "Pet",
                            confirmation = "unique-id-16",
                            whenCast = Instant.parse("2019-07-08T22:43:08Z"),
                            active = false,
                            rankings = listOf(
                                    Ranking(rank = 1, candidateName = "Cat"),
                                    Ranking(rank = 2, candidateName = "Bird"))),
                    Ballot(user = "Carol",
                            election = "Pet",
                            confirmation = "unique-id-17",
                            whenCast = Instant.parse("2019-07-08T22:45:08Z"),
                            active = false,
                            rankings = listOf(
                                    Ranking(rank = 2, candidateName = "Cat"),
                                    Ranking(rank = 3, candidateName = "Dog"),
                                    Ranking(rank = 1, candidateName = "Bird"))),
                    Ballot(user = "Dave",
                            election = "Pet",
                            confirmation = "unique-id-18",
                            whenCast = Instant.parse("2019-07-08T22:47:08Z"),
                            active = false,
                            rankings = listOf(
                                    Ranking(rank = 1, candidateName = "Cat"),
                                    Ranking(rank = 3, candidateName = "Dog"),
                                    Ranking(rank = 4, candidateName = "Bird"),
                                    Ranking(rank = 2, candidateName = "Reptile")))),
            preferences = listOf(
                    listOf(0, 1, 2, 3, 2),
                    listOf(3, 0, 4, 4, 4),
                    listOf(2, 0, 0, 3, 2),
                    listOf(0, 0, 0, 0, 0),
                    listOf(1, 0, 1, 1, 0)),
            strongestPaths = listOf(
                    listOf(0, 1, 2, 3, 2),
                    listOf(3, 0, 4, 4, 4),
                    listOf(2, 1, 0, 3, 2),
                    listOf(0, 0, 0, 0, 0),
                    listOf(1, 1, 1, 1, 0)),
            places = listOf(
                    Place(name = "1st", candidates = listOf("Cat")),
                    Place(name = "2nd", candidates = listOf("Bird", "Dog")),
                    Place(name = "4th", candidates = listOf("Reptile")),
                    Place(name = "5th", candidates = listOf("Fish"))))

    val actualTally = api.tally(alice, pet)

    val difference = ListDifference.compare(
            "expected",
            pretty.writeValueAsString(expectedTally),
            "actual  ",
            pretty.writeValueAsString(actualTally))

    assertTrue(difference.isSame, difference.messageLines.joinToString("\n"))
    assertEquals(50, api.lastSynced())
    SampleData.displayDebug().forEach(::execQuery)
}
