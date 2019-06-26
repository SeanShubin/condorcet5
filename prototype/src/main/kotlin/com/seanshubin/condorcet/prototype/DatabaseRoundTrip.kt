package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.util.db.JdbcConnectionLifecycle

fun main() {
    val host = "localhost"
    val database = "prototype"
    val user = "root"
    val password = "insecure"
    val lifecycle = JdbcConnectionLifecycle(host, user, password, database)
    lifecycle.withConnection { connection ->
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
}
