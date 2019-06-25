package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.addWhitespaceNoise
import com.seanshubin.condorcet.domain.Tester.createWithElection
import com.seanshubin.condorcet.domain.Tester.createWithUsers
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.nonOwnerCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import java.time.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class ElectionEndDateTest {
    @Test
    fun defaultsToNull() {
        // given
        val api = createWithUsers()

        // when
        val election = api.createElection(validCredentials, electionName)

        // then
        assertEquals(null, election.end)
    }

    @Test
    fun setEndDate() {
        // given
        val api = createWithElection()
        val end = Instant.parse("2019-06-07T16:05:41.325574Z")

        // when
        val election = api.setEndDate(validCredentials, electionName, end)

        // then
        assertEquals(end, election.end)
    }

    @Test
    fun setEndDateWithWhitespaceNoise() {
        // given
        val api = createWithElection()
        val end = Instant.parse("2019-06-07T16:05:41.325574Z")

        // when
        val election = api.setEndDate(validCredentials, electionName.addWhitespaceNoise(), end)

        // then
        assertEquals(end, election.end)
    }

    @Test
    fun setEndDateToNull() {
        // given
        val api = createWithElection()
        val end = Instant.parse("2019-06-07T16:05:41.325574Z")

        // when
        api.setEndDate(validCredentials, electionName, end)
        val election = api.setEndDate(validCredentials, electionName, null)

        // then
        assertEquals(null, election.end)
    }

    @Test
    fun setEndDateAuthentication() {
        // given
        val api = createWithElection()
        val end = Instant.parse("2019-06-07T16:05:41.325574Z")

        // when
        val result = Try { api.setEndDate(invalidCredentials, electionName, end) }

        // then
        assertEquals("Invalid user/password combination for 'Alice'", (result as Failure).exception.message)
    }

    @Test
    fun setEndDateAuthorization() {
        // given
        val api = createWithElection()
        val end = Instant.parse("2019-06-07T16:05:41.325574Z")

        // when
        val result = Try { api.setEndDate(nonOwnerCredentials, electionName, end) }

        // then
        assertEquals("User 'Bob' is not allowed to edit election 'New Election' owned by user 'Alice'", (result as Failure).exception.message)
    }

    @Test
    fun validateEndDateMissingElection() {
        // given
        val api = createWithElection()
        val end = Instant.parse("2019-06-07T16:05:41.325574Z")

        // when
        val result = Try { api.setEndDate(validCredentials, "No Election", end) }

        // then
        assertEquals("election 'No Election' not found", (result as Failure).exception.message)
    }
}
