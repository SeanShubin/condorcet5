package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.domain.memory.db.TableRow
import java.time.Instant

data class DbElection(val owner: String,
                      val name: String,
                      val end: Instant?,
                      val secret: Boolean,
                      val status: DbStatus) : TableRow<String> {
    override val primaryKey: String get() = name
    override val cells: List<Any?> = listOf(owner, name, end, secret, status)
}
