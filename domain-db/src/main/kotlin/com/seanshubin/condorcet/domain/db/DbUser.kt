package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.memory.TableRow

data class DbUser(val name: String,
                  val email: String,
                  val salt: String,
                  val hash: String) : TableRow<String> {
    override val primaryKey: String get() = name
    override val cells: List<Any?> = listOf(name, email, salt, hash)
}
