package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.createWithElection
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertEquals

class GetElectionTest {
    @Test
    fun notAuthorized() {
        // given
        val api = createWithElection()

        // when
        val result = Try { api.getElection(invalidCredentials, electionName) }

        // then
        assertEquals("Invalid user/password combination for 'Alice'", (result as Failure).exception.message)
    }

    @Test
    fun missingElection() {
        // given
        val api = createWithElection()

        // when
        val result = Try { api.getElection(validCredentials, "No Election") }

        // then
        assertEquals("election 'No Election' not found", (result as Failure).exception.message)
    }
}