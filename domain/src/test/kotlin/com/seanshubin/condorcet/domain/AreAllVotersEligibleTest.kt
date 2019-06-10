package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.domain.Tester.allEligibleVoterNames
import com.seanshubin.condorcet.domain.Tester.createWithElectionAndEligibleVoters
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AreAllVotersEligibleTest {
    @Test
    fun typical() {
        // given
        val api = createWithElectionAndEligibleVoters("Alice", "Bob")

        // when
        val result = api.getElection(validCredentials, electionName)

        // then
        assertFalse(result.isAllVoters)
    }

    @Test
    fun allVoters() {
        // given
        val api = createWithElectionAndEligibleVoters(*allEligibleVoterNames)

        // when
        val result = api.getElection(validCredentials, electionName)

        // then
        assertTrue(result.isAllVoters)
    }
}