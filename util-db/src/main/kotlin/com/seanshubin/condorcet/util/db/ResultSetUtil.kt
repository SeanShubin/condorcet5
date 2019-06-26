package com.seanshubin.condorcet.util.db

import java.sql.ResultSet

object ResultSetUtil {
    fun ResultSet.consumeToTable(): List<List<Any>> {
        val columnNames = columnNames()
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

    fun ResultSet.columnNames(): List<String> = (1..metaData.columnCount).map { metaData.getColumnName(it) }

    fun ResultSet.stringColumn(columnName: String): List<String> =
            stringColumn(indexOfColumn(columnName))

    fun ResultSet.stringColumn(columnIndex: Int): List<String> =
            map { getString(columnIndex) }

    fun ResultSet.indexOfColumn(columnName: String): Int =
            columnNames().indexOf(columnName)

    fun <T> ResultSet.map(f: () -> T): List<T> {
        val result = mutableListOf<T>()
        while (this.next()) {
            result.add(f())
        }
        return result
    }

    private fun getData(resultSet: ResultSet, columnNames: List<String>): List<List<Any>> {
        val iterator: Iterator<List<Any>> = ResultSetIterator(resultSet, columnNames)
        val list = ArrayList<List<Any>>()
        iterator.forEachRemaining { list.add(it) }
        return list
    }
}
