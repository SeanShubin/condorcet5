package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.memory.TableRow
import java.time.Instant

data class DbBallot(val user: String,
                    val election: String,
                    val confirmation: String,
                    val whenCast: Instant) : TableRow<DbVoter> {
    override val primaryKey: DbVoter get() = DbVoter(user, election)
    override val cells: List<Any?> = listOf(user, election, whenCast)
}
