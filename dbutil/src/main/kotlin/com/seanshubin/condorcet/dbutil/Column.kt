package com.seanshubin.condorcet.dbutil

interface Column {
    fun toSql(): List<String>
    fun sqlName(): String
}
