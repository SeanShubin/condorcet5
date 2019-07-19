package com.seanshubin.condorcet.domain.db

import java.util.*

data class Ranking(val rank: Int?, val candidateName: String) {
    companion object {
        val rankComparator = Comparator.nullsLast(Comparator.naturalOrder<Int>())

        fun List<Ranking>.unbiasedSort(random: Random): List<Ranking> {
            val groups: Map<Int?, List<Ranking>> = this.groupBy { it.rank }
            val keys = groups.keys.sortedWith(rankComparator)
            val results = mutableListOf<Ranking>()
            for (key in keys) {
                val rankingsForThisKey = groups.getValue(key).shuffled(random)
                results.addAll(rankingsForThisKey)
            }
            return results
        }
    }
}
