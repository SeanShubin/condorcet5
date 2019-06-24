package com.seanshubin.condorcet.util.rds

import java.sql.ResultSet

object ResultSetUtil {
    fun ResultSet.columnNames(): List<String> = (1..metaData.columnCount).map { metaData.getColumnName(it) }
    fun ResultSet.stringColumn(columnName: String): List<String> = stringColumn(indexOfColumn(columnName))
    fun ResultSet.stringColumn(columnIndex: Int): List<String> = map { getString(columnIndex) }
    fun ResultSet.indexOfColumn(columnName: String): Int = columnNames().indexOf(columnName)
    fun <T> ResultSet.map(f: () -> T): List<T> {
        val result = mutableListOf<T>()
        while (this.next()) {
            result.add(f())
        }
        return result
    }

}