package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.domain.memory.db.TableRow

data class DbBallot(val voterName: String,
                    val electionName: String,
                    val whenCast: String?) : TableRow<DbVoter> {
    override val primaryKey: DbVoter get() = DbVoter(voterName, electionName)
    override val cells: List<Any?> = listOf(voterName, electionName, whenCast)
}
