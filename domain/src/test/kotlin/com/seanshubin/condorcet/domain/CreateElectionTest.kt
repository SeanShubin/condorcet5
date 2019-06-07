package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.addWhitespaceNoise
import com.seanshubin.condorcet.domain.Tester.createWithUser
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertEquals


class CreateElectionTest {
    @Test
    fun createElectionReturnedEqualsRetrieved() {
        // given
        val api = createWithUser()

        // when
        val returnedElection = api.createElection(validCredentials, electionName)
        val retrievedElection = api.getElection(validCredentials, electionName)

        // then
        assertEquals(returnedElection, retrievedElection)
    }

    @Test
    fun createElection() {
        // given
        val api = createWithUser()

        // when
        val election = api.createElection(validCredentials, electionName)

        // then
        assertEquals(validCredentials.userName, election.ownerName)
        assertEquals(electionName, election.name)
        assertEquals(null, election.endIsoString)
        assertEquals(true, election.secretBallot)
        assertEquals(ElectionStatus.EDITING, election.status)
        assertEquals(emptyList(), election.candidateNames)
        assertEquals(emptyList(), election.voterNames)
    }

    @Test
    fun authCreateElection() {
        // given
        val api = createWithUser()

        // when
        val result = Try { api.createElection(invalidCredentials, electionName) }

        // then
        assertEquals("Invalid user/password combination for 'Alice'", (result as Failure).exception.message)
    }

    @Test
    fun createElectionTrimsWhitespace() {
        // given
        val api = createWithUser()

        // when
        val election = api.createElection(validCredentials, electionName.addWhitespaceNoise())

        // then
        assertEquals(electionName, election.name)
    }

    @Test
    fun noElectionsWithSameName() {
        // given
        val api = createWithUser()

        // when
        api.createElection(validCredentials, electionName)
        val result = Try { api.createElection(validCredentials, electionName) }

        // then
        assertEquals("Election with name '$electionName' already exists", (result as Failure).exception.message)
    }

    @Test
    fun noElectionsWithSameNameAfterTrimmed() {
        // given
        val api = createWithUser()

        // when
        api.createElection(validCredentials, electionName)
        val result = Try { api.createElection(validCredentials, electionName.addWhitespaceNoise()) }

        // then
        assertEquals("Election with name '$electionName' already exists", (result as Failure).exception.message)
    }

    @Test
    fun noElectionsWithSameNameWithDifferentCapitalization() {
        // given
        val api = createWithUser()

        // when
        api.createElection(validCredentials, electionName)
        val result = Try { api.createElection(validCredentials, electionName) }

        // then
        assertEquals("Election with name '$electionName' already exists", (result as Failure).exception.message)
    }

}