package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.memory.TableRow

data class DbVoter(val userName: String,
                   val electionName: String) : TableRow<DbVoter> {
    override val primaryKey: DbVoter get() = this
    override val cells: List<Any?> = listOf(userName, electionName)
}
