package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.table.formatter.RowStyleTableFormatter
import com.seanshubin.condorcet.util.db.ResultSetUtil.consumeToTable
import java.sql.DriverManager

fun main() {
    val url = "jdbc:mysql://prototype.cmph7klf3qhg.us-west-1.rds.amazonaws.com/prototype"
    val user = "prototype"
    val password = "prototype"
    val connection = DriverManager.getConnection(url, user, password)
    val sqlQuery = "select name, email, salt, hash from user where name = ?"
    val statement = connection.prepareStatement(sqlQuery)
    statement.setString(1, "alice")
    val resultSet = statement.executeQuery()
    val table = resultSet.consumeToTable()
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