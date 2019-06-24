package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.domain.db.Schema
import com.seanshubin.condorcet.util.db.Table

object Generator {
    fun dropTables() {
        fun toDropTableStatement(table: Table): String =
                "drop table if exists ${table.name};"
        Schema.tables.reversed().map(::toDropTableStatement).forEach(::println)
    }

    fun createTables() {
        Schema.tables.flatMap { it.toSql() }.forEach(::println)
    }

    fun createStatus() {
        val statusValues = listOf("editing", "live", "complete")
        fun insertStatus(status: String): String =
                "insert into status (name) values ('$status');"
        val statusSql = statusValues.map(::insertStatus)
        statusSql.forEach(::println)
    }

    fun sampleData() {
        val users = listOf("alice", "bob", "carol", "dave")
        fun insertUser(user: String): String =
                "insert into user (name, email, salt, hash) values ('$user', '$user@email.com', 'salt', 'hash');".trimMargin()

        val usersSql = users.map(::insertUser)

        val editingId = 1
        val liveId = 2
        val completeId = 3
        val aliceId = 1
        val bobId = 2
        val carolId = 3
        val daveId = 4
        val iceCreamId = 1
        val governmentId = 2
        val dystopiaId = 3
        val petId = 4
        val scienceFictionId = 5
        val fantasyId = 6

        fun createElection(id: Int, name: String, ownerId: Int, status: Int, candidates: List<String>, voters: List<Int>): List<String> {
            val insertElection = "insert into election (owner_id, name, end, secret, status_id) values ($ownerId, '$name', null, true, $status);"
            val insertCandidates = candidates.map {
                "insert into candidate (election_id, name) values ($id, '$it');"
            }
            val insertVoters = voters.map {
                "insert into voter (election_id, user_id) values ($id, $it);"
            }
            return listOf<String>() + insertElection + insertCandidates + insertVoters
        }

        val electionsSql = createElection(iceCreamId, "Favorite Ice Cream", aliceId, liveId, listOf("Chocolate", "Vanilla", "Strawberry"), listOf(aliceId, carolId)) +
                createElection(governmentId, "Government", aliceId, editingId, listOf("Monarchy", "Aristocracy", "Democracy"), listOf(aliceId, bobId, carolId, daveId)) +
                createElection(dystopiaId, "Dystopia", aliceId, completeId, listOf("1984", "Fahrenheit 451", "Brave New World"), listOf(aliceId, bobId, carolId, daveId)) +
                createElection(petId, "Pet", bobId, liveId, listOf("Cat", "Dog", "Bird", "Fish", "Reptile"), listOf(aliceId, bobId, daveId)) +
                createElection(scienceFictionId, "Science Fiction", carolId, editingId, listOf("Babylon 5", "Star Trek", "Blake''s 7", "Firefly"), listOf(aliceId, bobId, carolId, daveId)) +
                createElection(fantasyId, "Fantasy", daveId, liveId, listOf("Marvel Cinematic Universe", "Lord of the Rings", "Harry Potter"), listOf(aliceId, bobId, carolId, daveId))

        val sql = usersSql + electionsSql

        sql.forEach(::println)
    }

    fun all() {
        dropTables()
        createTables()
        createStatus()
        sampleData()
    }
}
