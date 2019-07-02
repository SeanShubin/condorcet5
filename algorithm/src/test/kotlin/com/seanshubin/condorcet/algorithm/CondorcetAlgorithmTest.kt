package com.seanshubin.condorcet.algorithm

import kotlin.test.Test
import kotlin.test.assertEquals

class CondorcetAlgorithmTest {
    @Test
    fun typical() {
        // given
        val election = "typical election"
        val candidates = setOf("frank", "grace", "eve")
        val eligibleVoters = setOf("bob", "dave", "alice", "carol")
        val aliceBallot = Ballot("alice", "code-4", mapOf(Pair("grace", 1), Pair("eve", 2), Pair("frank", 3)))
        val carolBallot = Ballot("carol", "code-3", mapOf(Pair("grace", 2), Pair("eve", 1), Pair("frank", 3)))
        val daveBallot = Ballot("dave", "code-5", mapOf(Pair("grace", 3), Pair("eve", 2), Pair("frank", 1)))
        val ballots = listOf(aliceBallot, carolBallot, daveBallot)
        val request = TallyElectionRequest(election, candidates, eligibleVoters, ballots)

        // when
        val response = CondorcetAlgorithm.tally(request)

        // then
        assertEquals("typical election", response.election)
        assertEquals(listOf("eve", "frank", "grace"), response.candidates)
        assertEquals(listOf("alice", "carol", "dave"), response.voted)
        assertEquals(listOf("bob"), response.didNotVote)
        assertEquals(
                listOf(Ranking(1, listOf("eve")),
                        Ranking(2, listOf("grace")),
                        Ranking(3, listOf("frank"))),
                response.rankings)
        assertEquals(listOf(carolBallot, aliceBallot, daveBallot), response.ballots)
        assertEquals(listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9)), response.preferenceMatrix)
        assertEquals(listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9)), response.strongestPathMatrix)
    }
    // input validation (philosophy: fail if anything looks like it might have been a mistake)
    // candidates must be unique
    // eligible voters must be unique
    // ballot voters must be unique
    // every ballot voter must match a voter
    // ballot confirmations must be unique
    // every ballot ranking must match a candidate
    // rankings must already be normalized

    // output validation (philosophy: accurate, predictable ordering)
    // candidates must be sorted
    // voted must be sorted
    // not voted must be sorted
    // rankings must be ascending by rank
    // candidates with same rank must be sorted
    // ballots must be sorted by confirmation
    // preference matrix must match candidate order
    // strongest path matrix must match candidate order

}