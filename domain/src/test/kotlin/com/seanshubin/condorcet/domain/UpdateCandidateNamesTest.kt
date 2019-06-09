package com.seanshubin.condorcet.domain

import arrow.core.Failure
import arrow.core.Try
import com.seanshubin.condorcet.domain.Tester.addWhitespaceNoise
import com.seanshubin.condorcet.domain.Tester.electionName
import com.seanshubin.condorcet.domain.Tester.invalidCredentials
import com.seanshubin.condorcet.domain.Tester.invertCapitalization
import com.seanshubin.condorcet.domain.Tester.nonOwnerCredentials
import com.seanshubin.condorcet.domain.Tester.validCredentials
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateCandidateNamesTest {
    @Test
    fun setCandidates() {
        // given
        val api = Tester.createWithElection()
        val candidates = listOf("Candidate A", "Candidate B", "Candidate C")

        // when
        val election = api.updateCandidateNames(validCredentials, electionName, candidates)

        // then
        assertEquals(candidates, election.candidateNames)
    }

    @Test
    fun setCandidatesAuthentication() {
        // given
        val api = Tester.createWithElection()
        val candidates = listOf("Candidate A", "Candidate B", "Candidate C")

        // when
        val result = Try { api.updateCandidateNames(invalidCredentials, electionName, candidates) }

        // then
        assertEquals("Invalid user/password combination for 'Alice'", (result as Failure).exception.message)
    }

    @Test
    fun setCandidatesAuthorization() {
        // given
        val api = Tester.createWithElection()
        val candidates = listOf("Candidate A", "Candidate B", "Candidate C")

        // when
        val result = Try { api.updateCandidateNames(nonOwnerCredentials, electionName, candidates) }

        // then
        assertEquals(
                "User 'Bob' is not allowed to edit election '$electionName' owned by user 'Alice'",
                (result as Failure).exception.message)
    }

    @Test
    fun setCandidatesWhitespace() {
        // given
        val api = Tester.createWithElection()
        val candidates = listOf("Candidate A", "Candidate B", "Candidate C")

        // when
        val election = api.updateCandidateNames(
                validCredentials,
                electionName,
                candidates.map { it.addWhitespaceNoise() })

        // then
        assertEquals(candidates, election.candidateNames)
    }

    @Test
    fun removeDuplicateCandidates() {
        // given
        val api = Tester.createWithElection()
        val candidates = listOf("Candidate A", "Candidate B", "Candidate C", "Candidate B")
        val candidatesWithoutDuplicates = listOf("Candidate A", "Candidate B", "Candidate C")

        // when
        val election = api.updateCandidateNames(
                validCredentials,
                electionName,
                candidates)

        // then
        assertEquals(candidatesWithoutDuplicates, election.candidateNames)
    }

    @Test
    fun removeDuplicateHandlesCapitalization() {
        // given
        val api = Tester.createWithElection()
        val candidates = listOf("Candidate A", "Candidate A".invertCapitalization())
        val candidatesWithoutDuplicates = listOf("Candidate A")

        // when
        val election = api.updateCandidateNames(
                validCredentials,
                electionName,
                candidates)

        // then
        assertEquals(candidatesWithoutDuplicates, election.candidateNames)
    }

    @Test
    fun removeDuplicateHandlesWhitespace() {
        // given
        val api = Tester.createWithElection()
        val candidates = listOf("Candidate A", "Candidate A".addWhitespaceNoise())
        val candidatesWithoutDuplicates = listOf("Candidate A")

        // when
        val election = api.updateCandidateNames(
                validCredentials,
                electionName,
                candidates)

        // then
        assertEquals(candidatesWithoutDuplicates, election.candidateNames)
    }

    @Test
    fun setCandidatesMissingElection() {
        // given
        val api = Tester.createWithElection()
        val candidates = listOf("Candidate A", "Candidate B", "Candidate C")

        // when
        val result = Try { api.updateCandidateNames(validCredentials, "No Election", candidates) }

        // then
        assertEquals("election 'No Election' not found", (result as Failure).exception.message)
    }
}
