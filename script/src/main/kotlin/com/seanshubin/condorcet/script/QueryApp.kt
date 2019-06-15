package com.seanshubin.condorcet.script

fun main() {
    withApi("query") { api ->
        api.listDatabases(databaseInstanceName)
    }
}
