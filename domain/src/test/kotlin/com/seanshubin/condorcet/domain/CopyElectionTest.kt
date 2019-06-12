package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.domain.Tester.createEmpty
import org.junit.Test
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
        api.updateCandidateNames(aliceCredentials, "Ice Cream", listOf("Chocolate", "Vanilla", "Strawberry"))
        api.setEndDate(aliceCredentials, "Ice Cream", "2019-06-11T22:57:26.497Z")
        api.setSecretBallot(aliceCredentials, "Ice Cream", secretBallot = false)
        api.updateEligibleVoters(aliceCredentials, "Ice Cream", listOf("Alice", "Carol"))
        api.doneEditingElection(aliceCredentials, "Ice Cream")

        val expected = ElectionDetail(
                ownerName = "Bob",
                name = "Copied Election",
                endIsoString = null,
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
}