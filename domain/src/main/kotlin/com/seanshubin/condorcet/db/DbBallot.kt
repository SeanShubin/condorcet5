package com.seanshubin.condorcet.db

import com.seanshubin.condorcet.memory.db.TableRow

data class DbBallot(val voterName: String,
                    val electionName: String,
                    val whenCast: String?) : TableRow<DbVoter> {
    override val primaryKey: DbVoter get() = DbVoter(voterName, electionName)
    override val cells: List<Any?> = listOf(voterName, electionName, whenCast)
}
