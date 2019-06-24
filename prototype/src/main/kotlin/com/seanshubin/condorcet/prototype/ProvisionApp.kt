package com.seanshubin.condorcet.prototype

fun main() {
    withApi("ProvisionApp") { api ->
        api.createInstance(databaseInstanceName)
    }
}
