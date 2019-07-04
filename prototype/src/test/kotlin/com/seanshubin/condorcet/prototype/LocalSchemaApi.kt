package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.domain.db.Schema
import com.seanshubin.condorcet.util.db.Table

object LocalSchemaApi : SchemaApi {
    override fun dropTables(): List<String> {
        fun toDropTableStatement(table: Table): String =
                "drop table if exists ${table.name};"
        return Schema.tables.reversed().map(::toDropTableStatement)
    }

    override fun createTables(): List<String> =
            Schema.tables.flatMap { it.toSql() }

    override fun createStaticData(): List<String> {
        val statusValues = listOf("editing", "live", "complete")
        fun insertStatus(status: String): String =
                "insert into status (name) values ('$status');"

        val statusSql = statusValues.map(::insertStatus)
        return statusSql

    }
}