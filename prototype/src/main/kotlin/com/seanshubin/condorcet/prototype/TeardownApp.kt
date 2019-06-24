package com.seanshubin.condorcet.prototype

fun main() {
    withApi("TeardownApp") { api ->
        api.deleteInstance(databaseInstanceName)
    }
}
