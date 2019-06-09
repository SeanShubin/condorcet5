package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.addWhitespaceNoise
import com.seanshubin.condorcet.domain.Tester.createWithElection
import com.seanshubin.condorcet.domain.Tester.createWithLiveElection
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.nonOwnerCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertEquals

class EndElectionTest {
    @Test
    fun electionMustBeLive() {
        // given
        val api = createWithElection()

        // when
        val result = Try { api.endElection(validCredentials, electionName) }

        // then
        assertEquals("Can not end election '$electionName', it is not live", (result as Failure).exception.message)
    }

    @Test
    fun typical() {
        // given
        val api = createWithLiveElection()

        // when
        val result = api.endElection(validCredentials, electionName)

        // then
        assertEquals(ElectionStatus.COMPLETE, result.status)
    }

    @Test
    fun whitespaceInName() {
        // given
        val api = createWithLiveElection()

        // when
        val result = api.endElection(validCredentials, electionName.addWhitespaceNoise())

        // then
        assertEquals(ElectionStatus.COMPLETE, result.status)
    }

    @Test
    fun authentication() {
        // given
        val api = createWithLiveElection()

        // when
        val result = Try { api.endElection(invalidCredentials, electionName) }

        // then
        assertEquals("Invalid user/password combination for '${invalidCredentials.userName}'", (result as Failure).exception.message)
    }

    @Test
    fun authorization() {
        // given
        val api = createWithLiveElection()

        // when
        val result = Try { api.endElection(nonOwnerCredentials, electionName) }

        // then
        assertEquals("User '${nonOwnerCredentials.userName}' " +
                "is not allowed to edit election '$electionName' " +
                "owned by user '${validCredentials.userName}'", (result as Failure).exception.message)
    }

    @Test
    fun missing() {
        // given
        val api = createWithLiveElection()

        // when
        val result = Try { api.endElection(validCredentials, "No election") }

        // then
        assertEquals("election 'No election' not found", (result as Failure).exception.message)
    }
}
