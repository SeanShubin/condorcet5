package com.seanshubin.condorcet.rdsutil

interface RdsDatabaseApi {
    fun createInstance(instanceIdentifier: String)
    fun instanceExists(instanceIdentifier: String): Boolean
    fun instanceStatus(instanceIdentifier: String): String
    fun deleteInstance(instanceIdentifier: String)
    fun waitForInstanceToGoAway(instanceIdentifier: String)
    fun waitForInstanceToBeAvailable(instanceIdentifier: String)
}
