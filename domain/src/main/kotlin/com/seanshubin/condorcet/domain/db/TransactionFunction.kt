package com.seanshubin.condorcet.domain.db

interface TransactionFunction {
    fun <T> inTransaction(f: () -> T): T
}
