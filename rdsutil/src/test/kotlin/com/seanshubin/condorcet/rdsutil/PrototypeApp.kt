package com.seanshubin.condorcet.rdsutil

import java.sql.DriverManager

fun main() {
    val prefix = "jdbc:mysql://"
    val endpoint = "mydb.cydy2gsb2x3r.us-west-2.rds.amazonaws.com"
    val db = "foo"
    val url = "$prefix$endpoint/foo"
    val user = "masteruser"
    val password = "masterpassword"

    val connection = DriverManager.getConnection(url, user, password)
    fun call(sql: String) {
        val statement = connection.prepareCall(sql)
        statement.execute()
    }

    fun query(sql: String) {
        val statement = connection.prepareStatement(sql)
        val resultSet = statement.executeQuery()
        val metaData = resultSet.metaData
        val columnCount = metaData.columnCount
        println("columnCount = $columnCount")
        (1..columnCount).forEach { columnIndex ->
            println("columnName[$columnIndex] = ${metaData.getColumnName(columnIndex)}")
        }

    }
//    call("create database foo")
    query("SELECT * FROM information_schema.columns")
}
