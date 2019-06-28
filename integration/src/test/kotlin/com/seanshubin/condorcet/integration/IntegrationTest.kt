package com.seanshubin.condorcet.integration

import com.seanshubin.condorcet.domain.Credentials

fun main() {
    LocalConnectionLifecycle.withConnection { connection ->
        fun execSql(sql: String) {
            val statement = connection.prepareStatement(sql)
            statement.execute()
        }
        LocalSchemaApi.dropTables().forEach(::execSql)
        LocalSchemaApi.createTables().forEach(::execSql)
        LocalSchemaApi.createStaticData().forEach(::execSql)

    }
    LocalApiLifecycle.withApi { api ->
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
        api.updateCandidateNames(aliceCredentials, electionName, listOf("Minor Improvements", "Radical Changes", "Status Quo"))
        api.updateEligibleVoters(aliceCredentials, electionName, voters)
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

    }
}

/*
election
╔══╤════════╤════╤═══╤══════╤═════════╗
║id│owner_id│name│end│secret│status_id║
╚══╧════════╧════╧═══╧══════╧═════════╝
event
╔══╤════╤════╗
║id│when│text║
╚══╧════╧════╝
status
╔══╤════╗
║id│name║
╚══╧════╝
user
╔══╤════╤═════╤════╤════╗
║id│name│email│salt│hash║
╚══╧════╧═════╧════╧════╝

 */