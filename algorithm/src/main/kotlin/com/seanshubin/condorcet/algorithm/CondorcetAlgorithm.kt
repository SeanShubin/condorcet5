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
        val preferenceMatrix = computePreferenceMatrix(candidates, ballots)
        val strongestPathMatrix = computeStrongestPaths(preferenceMatrix)
        val winners = computeWinners(strongestPathMatrix)
        val rankings = computePlacings(candidates, winners)

        return TallyElectionResponse(
                election,
                candidates,
                voted,
                didNotVote,
                rankings,
                ballots,
                preferenceMatrix.rows,
                strongestPathMatrix.rows)
    }

    private fun voted(eligibleVoters: Set<String>, ballots: List<Ballot>): VotedAndDidNotVote {
        fun ballotVoter(ballot: Ballot): String = ballot.voterName
        val voted = ballots.map(::ballotVoter)
        fun notVoted(voter: String): Boolean = !voted.contains(voter)
        val didNotVote = eligibleVoters.filter(::notVoted)
        return VotedAndDidNotVote(voted.sorted(), didNotVote.sorted())
    }

    private fun computePreferenceMatrix(candidates: List<String>, ballots: List<Ballot>): Matrix<Int> {
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

    private fun computeStrongestPaths(matrix: Matrix<Int>): Matrix<Int> {
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
        return Matrix(strongestPaths)
    }

    private fun computeWinners(strongestPaths: Matrix<Int>): List<List<Int>> {
        val soFar = emptyList<List<Int>>()
        val remain = (0 until strongestPaths.rows.size).toList()
        return computeWinners(strongestPaths, soFar, remain)
    }

    private fun computeWinners(strongestPaths: Matrix<Int>, soFar: List<List<Int>>, remain: List<Int>): List<List<Int>> {
        if (remain.isEmpty()) return soFar
        val winners = mutableListOf<Int>()
        val newRemain = remain.toMutableList()
        fun undefeated(target: Int): Boolean {
            for (i in 0 until remain.size) {
                val votesWon = strongestPaths[target, remain[i]]
                val votesLost = strongestPaths[remain[i], target]
                if (votesWon < votesLost) return false
            }
            return true
        }
        for (i in 0 until remain.size) {
            if (undefeated(remain[i])) {
                winners.add(remain[i])
                newRemain.remove(remain[i])
            }
        }
        return computeWinners(strongestPaths, soFar + listOf(winners), newRemain)
    }

    private fun computePlacings(candidates: List<String>, indicesPlaceList: List<List<Int>>): List<Placing> {
        val placings = mutableListOf<Placing>()
        var place = 1
        for (indicesAtPlace in indicesPlaceList) {
            val candidatesAtPlace = indicesAtPlace.map { candidates[it] }
            placings.add(Placing(place, candidatesAtPlace))
            place += indicesAtPlace.size

        }
        return placings
    }
}
