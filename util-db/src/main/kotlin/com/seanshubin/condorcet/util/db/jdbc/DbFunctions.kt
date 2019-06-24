package com.seanshubin.condorcet.util.db.jdbc

interface DbFunctions {
    fun <T> inTransaction(f: () -> T): T
    fun <T> inTransactionAlwaysRollback(f: () -> T): T
}
