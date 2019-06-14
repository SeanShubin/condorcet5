package com.seanshubin.condorcet.script

fun main() {
    withApi("TeardownApp") { api ->
        api.deleteInstance(databaseInstanceName)
    }
}
