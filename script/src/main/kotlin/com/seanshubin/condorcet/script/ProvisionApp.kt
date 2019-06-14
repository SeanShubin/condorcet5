package com.seanshubin.condorcet.script

fun main() {
    withApi("ProvisionApp") { api ->
        api.createInstance(databaseInstanceName)
    }
}
