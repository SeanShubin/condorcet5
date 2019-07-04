package com.seanshubin.condorcet.prototype

interface SchemaApi {
    fun dropTables(): List<String>
    fun createTables(): List<String>
    fun createStaticData(): List<String>
}