package com.seanshubin.condorcet.prototype

import java.sql.DriverManager

fun main() {
    val scheme = "jdbc:mysql"
    val host = "localhost"
    val database = "prototype"
    val url = "$scheme://$host/$database?serverTimezone=UTC"
    val user = "root"
    val password = "insecure"
    val connection = DriverManager.getConnection(url, user, password)

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
