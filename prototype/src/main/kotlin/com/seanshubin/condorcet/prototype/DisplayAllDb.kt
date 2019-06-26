package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.table.formatter.RowStyleTableFormatter
import com.seanshubin.condorcet.util.db.JdbcConnectionLifecycle
import com.seanshubin.condorcet.util.db.ResultSetUtil.consumeToList
import com.seanshubin.condorcet.util.db.ResultSetUtil.consumeToTable

fun main() {
    val host = "localhost"
    val user = "root"
    val password = "insecure"
    val rootLifecycle = JdbcConnectionLifecycle(host, user, password, "information_schema")
    val prototypeLifecycle = JdbcConnectionLifecycle(host, user, password, "prototype")
    val tableFormatter = RowStyleTableFormatter.boxDrawing

    val tables = rootLifecycle.withResultSet("select table_name from tables where table_schema = 'prototype'") { resultSet ->
        resultSet.consumeToList {
            resultSet.getString("table_name")
        }
    }
    prototypeLifecycle.withConnection { connection ->
        fun linesForTable(table: String): List<String> {
            val statement = connection.prepareStatement("select * from $table")
            val resultSet = statement.executeQuery()
            val cells = resultSet.consumeToTable()
            val formattedTable: List<String> = tableFormatter.format(cells)
            return listOf(table) + formattedTable
        }

        val lines = tables.flatMap(::linesForTable)
        lines.forEach(::println)
    }
}