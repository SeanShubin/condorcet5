package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.memory.TableRow

data class DbRanking(val voterName: String,
                     val electionName: String,
                     val candidateName: String,
                     val rank: Int) : TableRow<DbUserElectionCandidate> {
    override val primaryKey: DbUserElectionCandidate = DbUserElectionCandidate(voterName, electionName, candidateName)
    override val cells: List<Any?> = listOf(voterName, electionName, candidateName, rank)
}
