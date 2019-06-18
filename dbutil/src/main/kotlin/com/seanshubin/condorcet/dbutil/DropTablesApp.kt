package com.seanshubin.condorcet.dbutil

fun main() {
    fun toDropTableStatement(table: Table): String =
            "drop table if exists ${table.name};"
    Schema.tables.reversed().map(::toDropTableStatement).forEach(::println)
}
