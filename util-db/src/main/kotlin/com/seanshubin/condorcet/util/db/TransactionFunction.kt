package com.seanshubin.condorcet.util.db

interface TransactionFunction {
    fun <T> inTransaction(f: () -> T): T
}
