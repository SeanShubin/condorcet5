package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.memory.api.InMemoryDb
import org.junit.Test
import kotlin.test.assertEquals

class CreateElectionTest {
    val credentials = Credentials("Alice", "password")

    @Test
    fun createElectionReturnedEqualsRetrieved() {
        // given
        val api = createTestApi()

        // when
        val returnedElection = api.createElection(credentials, "Election Name")
        val retrievedElection = api.getElection(credentials, "Election Name")

        // then
        assertEquals(returnedElection, retrievedElection)
    }

    @Test
    fun createElection() {
        // given
        val api = createTestApi()

        // when
        val election = api.createElection(credentials, "Election Name")

        // then
        assertEquals(credentials.userName, election.ownerName)
        assertEquals("Election Name", election.name)
        assertEquals(null, election.endIsoString)
        assertEquals(true, election.secretBallot)
        assertEquals(ElectionStatus.EDITING, election.status)
        assertEquals(emptyList(), election.candidateNames)
        assertEquals(emptyList(), election.voterNames)
    }

    private fun createTestApi(): Api {
        val db = InMemoryDb()
        val api = ApiBackedByDb(db)
        api.register("ALice", "alice@email.com", "password")
        return api
    }

}