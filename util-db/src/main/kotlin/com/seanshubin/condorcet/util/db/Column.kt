package com.seanshubin.condorcet.util.db

interface Column {
    fun toSql(): List<String>
    fun sqlName(): String
}
