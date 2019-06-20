package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.memory.api.InMemoryDb
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class DeepTests {
    @Test
    fun contrastFirstPastThePost() {
        // given
        val api = createApi()
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
        vote(api, "Alice", electionName, Pair(1, "Minor Improvements"), Pair(2, "Status Quo"), Pair(3, "Radical Changes"))
        vote(api, "Bob", electionName, Pair(1, "Minor Improvements"), Pair(2, "Status Quo"), Pair(3, "Radical Changes"))
        vote(api, "Carol", electionName, Pair(1, "Minor Improvements"), Pair(2, "Status Quo"), Pair(3, "Radical Changes"))
        vote(api, "Dave", electionName, Pair(1, "Status Quo"), Pair(2, "Minor Improvements"), Pair(3, "Radical Changes"))
        vote(api, "Eve", electionName, Pair(1, "Status Quo"), Pair(2, "Minor Improvements"), Pair(3, "Radical Changes"))
        vote(api, "Frank", electionName, Pair(1, "Status Quo"), Pair(2, "Minor Improvements"), Pair(3, "Radical Changes"))
        vote(api, "Grace", electionName, Pair(1, "Radical Changes"), Pair(2, "Minor Improvements"), Pair(3, "Status Quo"))
        vote(api, "Heidi", electionName, Pair(1, "Radical Changes"), Pair(2, "Minor Improvements"), Pair(3, "Status Quo"))
        vote(api, "Ivy", electionName, Pair(1, "Radical Changes"), Pair(2, "Minor Improvements"), Pair(3, "Status Quo"))
        vote(api, "Judy", electionName, Pair(1, "Radical Changes"), Pair(2, "Minor Improvements"), Pair(3, "Status Quo"))
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

    private fun vote(api: Api, userName: String, electionName: String, vararg rankingPairs: Pair<Int, String>) {
        val credentials = Credentials(userName, "password")
        val rankings = rankingPairs.map {
            Ranking(it.first, it.second)
        }
        api.castBallot(credentials, electionName, userName, rankings)
    }

    private fun createApi(): Api {
        val now = Instant.parse("2019-06-10T15:53:01.806Z")
        val clock = StoppedClock(now)
        val oneWayHash: OneWayHash = Sha256Hash()
        val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
        val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        val db = InMemoryDb()
        val api = ApiBackedByDb(db, clock, passwordUtil)
        return api

    }
}
