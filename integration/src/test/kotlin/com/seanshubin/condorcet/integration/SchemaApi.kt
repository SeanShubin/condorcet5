package com.seanshubin.condorcet.integration

interface SchemaApi {
    fun dropTables(): List<String>
    fun createTables(): List<String>
    fun createStaticData(): List<String>
}