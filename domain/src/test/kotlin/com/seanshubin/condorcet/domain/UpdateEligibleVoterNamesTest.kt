package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.addWhitespaceNoise
import com.seanshubin.condorcet.domain.Tester.createWithElectionAndSeveralUsers
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.invertCapitalization
import com.seanshubin.condorcet.domain.Tester.nonOwnerCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateEligibleVoterNamesTest {
    @Test
    fun setEligibleVoters() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice", "Carol")

        // when
        val election = api.setVoters(validCredentials, electionName, voters)

        // then
        assertEquals(voters, election.voterNames)
    }

    @Test
    fun setEligibleVotersToAll() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice", "Bob", "Carol", "Dave")

        // when
        val election = api.setVotersToAll(validCredentials, electionName)

        // then
        assertEquals(voters, election.voterNames)
    }

    @Test
    fun setVoterAuthentication() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice", "Carol")

        // when
        val result = Try { api.setVoters(invalidCredentials, electionName, voters) }

        // then
        assertEquals("Invalid user/password combination for 'Alice'", (result as Failure).exception.message)
    }

    @Test
    fun setVoterAuthorization() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice", "Carol")

        // when
        val result = Try { api.setVoters(nonOwnerCredentials, electionName, voters) }

        // then
        assertEquals(
                "User 'Bob' is not allowed to edit election '$electionName' owned by user 'Alice'",
                (result as Failure).exception.message)
    }

    @Test
    fun setVoterWhitespace() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice Smith", "Carol Jones")

        // when
        val election = api.setVoters(
                validCredentials,
                electionName,
                voters.map { it.addWhitespaceNoise() })

        // then
        assertEquals(voters, election.voterNames)
    }

    @Test
    fun removeDuplicateVoters() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice", "Bob", "Alice", "Bob", "Alice")
        val votersWithoutDuplicates = listOf("Alice", "Bob")

        // when
        val election = api.setVoters(
                validCredentials,
                electionName,
                voters)

        // then
        assertEquals(votersWithoutDuplicates, election.voterNames)
    }

    @Test
    fun removeDuplicateHandlesCapitalization() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice", "Alice".invertCapitalization())
        val votersWithoutDuplicates = listOf("Alice")

        // when
        val election = api.setVoters(
                validCredentials,
                electionName,
                voters)

        // then
        assertEquals(votersWithoutDuplicates, election.voterNames)
    }

    @Test
    fun removeDuplicateHandlesWhitespace() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice", "Alice".addWhitespaceNoise())
        val votersWithoutDuplicates = listOf("Alice")

        // when
        val election = api.setVoters(
                validCredentials,
                electionName,
                voters)

        // then
        assertEquals(votersWithoutDuplicates, election.voterNames)
    }

    @Test
    fun setVotersMissingElection() {
        // given
        val api = createWithElectionAndSeveralUsers()
        val voters = listOf("Alice")

        // when
        val result = Try { api.setVoters(validCredentials, "No Election", voters) }

        // then
        assertEquals("election 'No Election' not found", (result as Failure).exception.message)
    }

    @Test
    fun setVotersToAllMissingElection() {
        // given
        val api = createWithElectionAndSeveralUsers()

        // when
        val result = Try { api.setVotersToAll(validCredentials, "No Election") }

        // then
        assertEquals("election 'No Election' not found", (result as Failure).exception.message)
    }
}
