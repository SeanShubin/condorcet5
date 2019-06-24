package com.seanshubin.condorcet.prototype

fun main() {
    withApi("query") { api ->
        api.listDatabases(databaseInstanceName)
    }
}
