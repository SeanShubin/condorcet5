package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.allEligibleVoterNames
import com.seanshubin.condorcet.domain.Tester.createWithElectionAndEligibleVoters
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AreAllVotersEligibleTest {
    @Test
    fun typical() {
        // given
        val api = createWithElectionAndEligibleVoters("Alice", "Bob")

        // when
        val result = api.areAllVotersEligible(validCredentials, electionName)

        // then
        assertFalse(result)
    }

    @Test
    fun allVoters() {
        // given
        val api = createWithElectionAndEligibleVoters(*allEligibleVoterNames)

        // when
        val result = api.areAllVotersEligible(validCredentials, electionName)

        // then
        assertTrue(result)
    }

    @Test
    fun notAuthorized() {
        // given
        val api = createWithElectionAndEligibleVoters("Alice", "Bob")

        // when
        val result = Try { api.areAllVotersEligible(invalidCredentials, electionName) }

        // then
        assertEquals("Invalid user/password combination for 'Alice'", (result as Failure).exception.message)
    }

    @Test
    fun missingElection() {
        // given
        val api = createWithElectionAndEligibleVoters("Alice", "Bob")

        // when
        val result = Try { api.areAllVotersEligible(validCredentials, "No Election") }

        // then
        assertEquals("election 'No Election' not found", (result as Failure).exception.message)
    }
}