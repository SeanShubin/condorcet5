package com.seanshubin.condorcet.rdsutil

interface RdsDatabaseApi {
    fun createDatabase(instanceIdentifier: String, masterUserPassword: String)

    fun databaseExists(instanceIdentifier: String): Boolean

    fun deleteDatabase(instanceIdentifier: String)

    fun waitForDatabaseToGoAway(instanceIdentifier: String)
}
