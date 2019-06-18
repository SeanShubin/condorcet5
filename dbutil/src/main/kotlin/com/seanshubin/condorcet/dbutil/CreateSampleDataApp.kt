package com.seanshubin.condorcet.dbutil

fun main() {
    val statusValues = listOf("editing", "live", "complete")
    fun insertStatus(status: String): String =
            """insert into status (name) values ("$status");"""
    statusValues.map(::insertStatus).forEach(::println)

    val users = listOf("alice", "bob", "carol", "dave")
    fun insertUser(user: String): String =
            """insert into users (name, email, password) values ("$user", "$user@email.com", "password");""".trimMargin()
    users.map(::insertUser).forEach(::println)
}