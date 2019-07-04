package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.table.formatter.RowStyleTableFormatter
import com.seanshubin.condorcet.util.ClassLoaderUtil
import com.seanshubin.condorcet.util.db.JdbcConnectionLifecycle
import com.seanshubin.condorcet.util.db.ResultSetUtil.consumeToList
import com.seanshubin.condorcet.util.db.ResultSetUtil.consumeToTable
import java.nio.file.Paths

fun main() {
    val host = "localhost"
    val user = "root"
    val password = "insecure"
    val rootLifecycle = JdbcConnectionLifecycle(host, user, password, "information_schema")
    val prototypeLifecycle = JdbcConnectionLifecycle(host, user, password, "prototype")
    val tableFormatter = RowStyleTableFormatter.boxDrawing
    val logPath = Paths.get("out", "log")
    val logger = LoggerFactory.create(logPath, "display-all-db")

    val tables = rootLifecycle.withResultSet("select table_name from tables where table_schema = 'prototype'") { resultSet ->
        resultSet.consumeToList {
            resultSet.getString("table_name")
        }
    }
    prototypeLifecycle.withConnection { connection ->
        fun tableLinesForSql(sql: String): List<String> {
            val statement = connection.prepareStatement(sql)
            val resultSet = statement.executeQuery()
            val cells = resultSet.consumeToTable()
            val formattedTable: List<String> = tableFormatter.format(cells)
            return listOf(sql) + formattedTable
        }

        fun linesForTable(table: String): List<String> {
            val sql = "select * from $table"
            return tableLinesForSql(sql)
        }

        val rawDataLines = tables.flatMap(::linesForTable)

        fun linesForDebugQuery(table: String): List<String> {
            val sqlResource = "debug-$table.sql"
            val sql = ClassLoaderUtil.loadResourceAsString(sqlResource)
            return tableLinesForSql(sql)
        }

        val debugQueryLines = tables.flatMap(::linesForDebugQuery)

        val lines = rawDataLines + debugQueryLines

        lines.forEach(logger::log)
    }
}