package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.createWithElection
import com.seanshubin.condorcet.domain.Tester.createWithUsers
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertEquals


class ElectionSecretBallotTest {
    @Test
    fun defaultsToTrue() {
        // given
        val api = createWithUsers()

        // when
        val election = api.createElection(validCredentials, electionName)

        // then
        assertEquals(true, election.secretBallot)
    }

    @Test
    fun setSecretBallot() {
        // given
        val api = createWithElection()

        // when
        val election = api.setSecretBallot(validCredentials, electionName, false)

        // then
        assertEquals(false, election.secretBallot)
    }

    @Test
    fun setSecretBallotAuth() {
        // given
        val api = createWithElection()

        // when
        val result = Try { api.setSecretBallot(invalidCredentials, electionName, false) }

        // then
        assertEquals("Invalid user/password combination for 'Alice'", (result as Failure).exception.message)
    }

    @Test
    fun validateSecretBallotMissingElection() {
        // given
        val api = createWithElection()

        // when
        val result = Try { api.setSecretBallot(validCredentials, "No Election", false) }

        // then
        assertEquals("election 'No Election' not found", (result as Failure).exception.message)
    }
}
