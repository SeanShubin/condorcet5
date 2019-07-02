package com.seanshubin.condorcet.algorithm

import com.seanshubin.condorcet.matrix.Matrix
import com.seanshubin.condorcet.matrix.Size
import com.seanshubin.condorcet.matrix.plus
import kotlin.math.max
import kotlin.math.min

object CondorcetAlgorithm {
    fun tally(request: TallyElectionRequest): TallyElectionResponse {
        val election = request.election
        val candidates = request.candidates.sorted()
        val eligibleVoters = request.eligibleVoters
        val ballots = request.ballots.sortedWith(Ballot.sortByConfirmation)
        val (voted, didNotVote) = voted(eligibleVoters, ballots)
        val initialMatrix = initialMatrix(candidates, ballots)
        println(candidates)
        println(initialMatrix)
        val computed = compute(initialMatrix)
        val computedRankings = computed.rankings
        val preferenceMatrix = computed.preferenceMatrix
        val strongestPathMatrix = computed.strongestPathMatrix
        val rankings = composeRankings(candidates, computedRankings)

        return TallyElectionResponse(
                election,
                candidates,
                voted,
                didNotVote,
                rankings,
                ballots,
                preferenceMatrix,
                strongestPathMatrix)
    }

    private fun voted(eligibleVoters: Set<String>, ballots: List<Ballot>): VotedAndDidNotVote {
        fun ballotVoter(ballot: Ballot): String = ballot.voterName
        val voted = ballots.map(::ballotVoter)
        fun notVoted(voter: String): Boolean = !voted.contains(voter)
        val didNotVote = eligibleVoters.filter(::notVoted)
        return VotedAndDidNotVote(voted, didNotVote)
    }

    private fun initialMatrix(candidates: List<String>, ballots: List<Ballot>): Matrix<Int> {
        val lose = Int.MAX_VALUE
        val size = Size(candidates.size, candidates.size)
        val emptyMatrix = Matrix(size, 0)
        fun addBallot(accumulator: Matrix<Int>, ballot: Ballot): Matrix<Int> {
            fun computeValue(row: Int, col: Int): Int {
                val winner = candidates[row]
                val loser = candidates[col]
                return if (ballot.rankings[winner] ?: lose < ballot.rankings[loser] ?: lose) 1 else 0
            }
            return accumulator + Matrix(size, ::computeValue)
        }
        return ballots.fold(emptyMatrix, ::addBallot)
    }

    private fun compute(matrix: Matrix<Int>): Computed {
        val strongestPaths = matrix.mutableCopy()
        val size = matrix.size.row
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (i != j) {
                    for (k in 0 until size) {
                        if (i != k && j != k) {
                            strongestPaths[j][k] =
                                    max(strongestPaths[j][k], min(strongestPaths[j][i], strongestPaths[i][k]))
                        }
                    }
                }
            }
        }
        val rankings = composeRankings()
        return Computed(matrix, Matrix(matrix.rows))
    }

    private fun composeRankings(candidates: List<String>, computedRankings: Map<Int, List<Int>>): List<Ranking> {
        TODO("not implemented")
    }
}
