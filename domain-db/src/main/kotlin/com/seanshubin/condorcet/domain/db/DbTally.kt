package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.memory.TableRow

data class DbTally(val electionName: String,
                   val report: String) : TableRow<String> {
    override val primaryKey: String get() = electionName
    override val cells: List<Any?> = listOf(electionName, report)
}
