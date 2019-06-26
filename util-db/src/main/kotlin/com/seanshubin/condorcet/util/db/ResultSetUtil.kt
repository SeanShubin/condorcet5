package com.seanshubin.condorcet.util.db

import java.sql.ResultSet

object ResultSetUtil {
    fun ResultSet.consumeToTable(): List<List<Any>> {
        val columnNames = getColumnNames(this)
        val data = getData(this, columnNames)
        return listOf(columnNames) + data
    }

    fun <T> ResultSet.consumeToList(transform: (ResultSet) -> T): List<T> {
        val list = mutableListOf<T>()
        while (next()) {
            list.add(transform(this))
        }
        return list
    }

    private fun getColumnNames(resultSet: ResultSet): List<String> {
        val metaData = resultSet.metaData
        val columnCount = metaData.columnCount
        val extractColumnName = { columnIndex: Int -> metaData.getColumnName(columnIndex) }
        val columnNames = (1..columnCount).map(extractColumnName)
        return columnNames
    }

    private fun getData(resultSet: ResultSet, columnNames: List<String>): List<List<Any>> {
        val iterator: Iterator<List<Any>> = ResultSetIterator(resultSet, columnNames)
        val list = ArrayList<List<Any>>()
        iterator.forEachRemaining { list.add(it) }
        return list
    }
}
