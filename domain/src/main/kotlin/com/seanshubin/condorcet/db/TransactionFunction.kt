package com.seanshubin.condorcet.db

interface TransactionFunction {
    fun <T> inTransaction(f: () -> T): T
}
