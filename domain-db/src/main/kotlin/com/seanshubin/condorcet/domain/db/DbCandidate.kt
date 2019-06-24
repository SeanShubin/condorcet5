package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.memory.TableRow

data class DbCandidate(val name: String,
                       val electionName: String) : TableRow<DbCandidate> {
    override val primaryKey: DbCandidate get() = this
    override val cells: List<Any?> = listOf(name, electionName)
}
