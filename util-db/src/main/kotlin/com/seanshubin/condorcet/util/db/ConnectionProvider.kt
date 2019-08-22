package com.seanshubin.condorcet.util.db

interface ConnectionProvider {
    fun getConnection(): ConnectionWrapper
}
