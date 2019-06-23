package com.seanshubin.condorcet.domain.jdbc

interface DbFunctions {
    fun <T> inTransaction(f: () -> T): T
    fun <T> inTransactionAlwaysRollback(f: () -> T): T
}
