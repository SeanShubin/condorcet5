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
import kotlin.test.Test
import kotlin.test.assertEquals

class DoneEditingElectionTest {
    @Test
    fun default() {
        // given
        val api = createWithUsers()

        // when
        val result = api.createElection(validCredentials, electionName)

        // then
        assertEquals(ElectionStatus.EDITING, result.status)
    }

    @Test
    fun typical() {
        // given
        val api = createWithElection()

        // when
        val result = api.doneEditingElection(validCredentials, electionName)

        // then
        assertEquals(ElectionStatus.LIVE, result.status)
    }

    @Test
    fun whitespaceInName() {
        // given
        val api = createWithElection()

        // when
        val result = api.doneEditingElection(validCredentials, electionName.addWhitespaceNoise())

        // then
        assertEquals(ElectionStatus.LIVE, result.status)
    }

    @Test
    fun authentication() {
        // given
        val api = createWithElection()

        // when
        val result = Try { api.doneEditingElection(invalidCredentials, electionName) }

        // then
        assertEquals("Invalid user/password combination for '${invalidCredentials.userName}'", (result as Failure).exception.message)
    }

    @Test
    fun authorization() {
        // given
        val api = createWithElection()

        // when
        val result = Try { api.doneEditingElection(nonOwnerCredentials, electionName) }

        // then
        assertEquals("User '${nonOwnerCredentials.userName}' " +
                "is not allowed to edit election '$electionName' " +
                "owned by user '${validCredentials.userName}'", (result as Failure).exception.message)
    }

    @Test
    fun missing() {
        // given
        val api = createWithElection()

        // when
        val result = Try { api.doneEditingElection(validCredentials, "No election") }

        // then
        assertEquals("election 'No election' not found", (result as Failure).exception.message)
    }

    // todo: can not go live with no time
}
