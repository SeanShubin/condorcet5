package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.util.db.JdbcConnectionLifecycle

fun main() {
    val host = "localhost"
    val user = "root"
    val password = "insecure"
    val rootLifecycle = JdbcConnectionLifecycle(host, user, password)
    val prototypeLifecycle = JdbcConnectionLifecycle("$host/prototype", user, password)

    val tableNamesStatement = rootLifecycle.withResultSet("select table_name from tables where table_schema = 'prototype'") {

    }
    val resultSet = tableNamesStatement.executeQuery()
    fun exec(sql: String) {
        val statement = connection.prepareStatement(sql)
        try {
            val result = statement.execute()
            if (result) {
                println("true:      $sql")
            } else {
                println("false:     $sql")
            }
        } catch (ex: Exception) {
            println("exception: $sql")
            ex.printStackTrace()
            throw ex
        }
    }

    val sqlLines = Generator.all()
    val sql = sqlLines.joinToString("\n")
    val sqlStatements = sql.split(";").filter { it.isNotBlank() }

    sqlStatements.forEach(::exec)

}