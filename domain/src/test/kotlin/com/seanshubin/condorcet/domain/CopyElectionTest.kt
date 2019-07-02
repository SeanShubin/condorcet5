package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.createEmpty
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals

class CopyElectionTest {
    @Test
    fun typical() {
        // given
        val api = createEmpty()
        val aliceCredentials = Credentials("Alice", "password")
        val bobCredentials = Credentials("Bob", "password")
        api.register(aliceCredentials.userName, "alice@email.com", aliceCredentials.userPassword)
        api.register("Bob", "bob@email.com", "password")
        api.register("Carol", "carol@email.com", "password")
        api.register("Dave", "dave@email.com", "password")
        api.createElection(aliceCredentials, "Ice Cream")
        api.setCandidateNames(aliceCredentials, "Ice Cream", listOf("Chocolate", "Vanilla", "Strawberry"))
        api.setEndDate(aliceCredentials, "Ice Cream", Instant.parse("2019-06-11T22:57:26.497Z"))
        api.setSecretBallot(aliceCredentials, "Ice Cream", secretBallot = false)
        api.setVoters(aliceCredentials, "Ice Cream", listOf("Alice", "Carol"))
        api.doneEditingElection(aliceCredentials, "Ice Cream")

        val expected = ElectionDetail(
                ownerName = "Bob",
                name = "Copied Election",
                end = null,
                secretBallot = false,
                status = ElectionStatus.EDITING,
                candidateNames = listOf("Chocolate", "Vanilla", "Strawberry"),
                voterNames = listOf("Alice", "Carol"),
                isAllVoters = false)

        // when
        val copied = api.copyElection(bobCredentials, "Copied Election", "Ice Cream")

        // then
        assertEquals(expected, copied)
    }

    @Test
    fun electionAlreadyExists() {
        // given
        val api = createEmpty()
        val aliceCredentials = Credentials("Alice", "password")
        api.register(aliceCredentials.userName, "alice@email.com", aliceCredentials.userPassword)
        api.createElection(aliceCredentials, "Ice Cream")

        api.createElection(aliceCredentials, "Already Exists")

        // when
        val result = Try { api.copyElection(aliceCredentials, "Already Exists", "Ice Cream") }

        // then
        assertEquals("Election with name 'Already Exists' already exists", (result as Failure).exception.message)
    }

    @Test
    fun electionToCopyDoesNotExist() {
        // given
        val api = createEmpty()
        val aliceCredentials = Credentials("Alice", "password")
        api.register(aliceCredentials.userName, "alice@email.com", aliceCredentials.userPassword)

        // when
        val result = Try { api.copyElection(aliceCredentials, "Copied Election", "Ice Cream") }

        // then
        assertEquals("election 'Ice Cream' not found", (result as Failure).exception.message)
    }
}