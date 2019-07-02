package com.seanshubin.condorcet.algorithm

object CondorcetAlgorithm {
    fun tally(request: TallyElectionRequest): TallyElectionResponse {
        val election = request.election
        val candidates = request.candidates.sorted()
        val eligibleVoters = request.eligibleVoters.sorted()
        val ballots = request.ballots.sortedWith(Ballot.sortByConfirmation)
        val (voted, didNotVote) = voted(eligibleVoters, ballots)
        val initialMatrix = initialMatrix(candidates, ballots)
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

    private fun voted(eligibleVoters: List<String>, ballots: List<Ballot>): VotedAndNotVoted {
        TODO("not implemented")
    }

    private fun initialMatrix(candidates: List<String>, ballots: List<Ballot>): List<List<Int>> {
        TODO("not implemented")
    }

    private fun compute(matrix: List<List<Int>>): Computed {
        TODO("not implemented")
    }

    private fun composeRankings(candidates: List<String>, computedRankings: Map<Int, List<Int>>): List<Ranking> {
        TODO("not implemented")
    }
}
