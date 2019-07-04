package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.table.formatter.RowStyleTableFormatter
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ResultSetIterator
import java.nio.file.Paths

fun main() {
    val logger = LoggerFactory.create(Paths.get("out", "log"), "sample-data")
    val emit: (String) -> Unit = logger::log
    fun sqlEvent(sql: String): Unit = emit(sql)
    ConnectionFactory.withConnection(
            Connections.local,
            ::sqlEvent) { connection ->
        fun execQuery(sql: String) {
            connection.execQuery(sql) { resultSet ->
                val iterator = ResultSetIterator.consume(resultSet)
                val header = iterator.columnNames
                val table = iterator.consumeRemainingToTable()
                val formattedTable = RowStyleTableFormatter.boxDrawing.format(listOf(header) + table)
                formattedTable.forEach(emit)
            }
        }

        fun execUpdate(sql: String) {
            connection.execUpdate(sql)
        }
        connection.execUpdate("create database if not exists sample")
        connection.execUpdate("use sample")
        SampleData.dropTables().forEach(::execUpdate)
        SampleData.createTables().forEach(::execUpdate)
        SampleData.staticData().forEach(::execUpdate)
        SampleData.displayGeneric().forEach(::execQuery)
        SampleData.displayDebug().forEach(::execQuery)
    }
}