package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.domain.db.Schema
import com.seanshubin.condorcet.util.ClassLoaderUtil

object SampleData {
    fun dropTables(): List<String> {
        return Schema.tables.reversed().map { it.toDropTableStatement() }
    }

    fun createTables(): List<String> {
        return Schema.tables.flatMap { it.toCreateTableStatements() }
    }

    fun staticData(): List<String> {
        val statusValues = listOf("editing", "live", "complete")
        fun insertStatus(status: String): String =
                "insert into status (name) values ('$status')"

        val statusSql = statusValues.map(::insertStatus)
        return statusSql
    }

    fun displayGeneric(): List<String> = Schema.tables.map { "select * from ${it.name}" }

    fun displayDebug(): List<String> =
            Schema.tables.map { table ->
                val sqlResource = "debug-${table.name}.sql"
                val sql = ClassLoaderUtil.loadResourceAsString(sqlResource)
                sql
            }
}
