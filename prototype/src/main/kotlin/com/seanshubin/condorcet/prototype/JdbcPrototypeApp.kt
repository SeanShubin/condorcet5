package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.table.formatter.RowStyleTableFormatter
import java.sql.DriverManager
import java.sql.ResultSet

class ResultSetIterator(
        private val resultSet: ResultSet,
        private val columnNames: List<String>
) : Iterator<List<Any>> {
    private var resultSetNext = resultSet.next()
    override fun hasNext(): Boolean = resultSetNext

    override fun next(): List<Any> {
        val rowCells = columnNames.map { resultSet.getString(it) }
        resultSetNext = resultSet.next()
        return rowCells
    }
}

object ResultSetUtil {
    fun toTable(resultSet: ResultSet): List<List<Any>> {
        val columnNames = getColumnNames(resultSet)
        val data = getData(resultSet, columnNames)
        return listOf(columnNames) + data
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

fun main() {
    val url = "jdbc:mysql://prototype.cmph7klf3qhg.us-west-1.rds.amazonaws.com/prototype"
    val user = "prototype"
    val password = "prototype"
    val connection = DriverManager.getConnection(url, user, password)
    val sqlQuery = "select name, email, salt, hash from user where name = ?"
    val statement = connection.prepareStatement(sqlQuery)
    statement.setString(1, "alice")
    val resultSet = statement.executeQuery()
    val table = ResultSetUtil.toTable(resultSet)
    val tableFormatter = RowStyleTableFormatter.boxDrawing
    val lines = tableFormatter.format(table)
    lines.forEach(::println)

//    val sqlUpdate = "insert into user (name, email, salt, hash) values (?, ?, ?, ?)"
//    val updateStatement = connection.prepareStatement(sqlUpdate)
//    updateStatement.setObject(1, "alice")
//    updateStatement.setObject(2, "alice@email.com")
//    updateStatement.setObject(3, "salt")
//    updateStatement.setObject(4, "hash")
//    updateStatement.executeUpdate()
}

//insert into status (name) values ('editing');