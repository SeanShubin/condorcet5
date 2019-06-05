package com.seanshubin.condorcet.db

import com.seanshubin.condorcet.memory.db.TableRow

data class DbTally(val electionName: String,
                   val candidateName: String,
                   val rank: Int) : TableRow<DbElectionCandidate> {
    override val primaryKey: DbElectionCandidate get() = DbElectionCandidate(electionName, candidateName)
    override val cells: List<Any?> = listOf(electionName, candidateName, rank)
}
