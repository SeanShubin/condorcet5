package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.memory.api.InMemoryDb
import org.junit.Test
import kotlin.test.assertEquals

class CreateElectionTest {
    val credentials = Credentials("Alice", "password")
    val invalidCredentials = Credentials("foo", "bar")

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

    @Test
    fun authCreateElection() {
        // given
        val api = createTestApi()

        // when
        val result = Try { api.createElection(invalidCredentials, "Election Name") }

        // then
        assertEquals("Invalid user/password combination for 'foo'", (result as Failure).exception.message)
    }

    @Test
    fun createElectionTrimsWhitespace() {
        // given
        val api = createTestApi()

        // when
        val election = api.createElection(credentials, "  Election   Name  ")

        // then
        assertEquals("Election Name", election.name)
    }

    @Test
    fun noElectionsWithSameName() {
        // given
        val api = createTestApi()

        // when
        api.createElection(credentials, "Election Name")
        val result = Try { api.createElection(credentials, "Election Name") }

        // then
        assertEquals("Election with name 'Election Name' already exists", (result as Failure).exception.message)
    }

    @Test
    fun noElectionsWithSameNameAfterTrimmed() {
        // given
        val api = createTestApi()

        // when
        api.createElection(credentials, "Election Name")
        val result = Try { api.createElection(credentials, "  Election  Name  ") }

        // then
        assertEquals("Election with name 'Election Name' already exists", (result as Failure).exception.message)
    }

    @Test
    fun noElectionsWithSameNameWithDifferentCapitalization() {
        // given
        val api = createTestApi()

        // when
        api.createElection(credentials, "Election Name")
        val result = Try { api.createElection(credentials, "election name") }

        // then
        assertEquals("Election with name 'election name' already exists", (result as Failure).exception.message)
    }

    private fun createTestApi(): Api {
        val db = InMemoryDb()
        val api = ApiBackedByDb(db)
        api.register("ALice", "alice@email.com", "password")
        return api
    }

}