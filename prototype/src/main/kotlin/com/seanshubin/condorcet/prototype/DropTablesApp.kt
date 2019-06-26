package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.domain.db.Schema
import com.seanshubin.condorcet.util.db.JdbcConnectionLifecycle
import com.seanshubin.condorcet.util.db.Table

fun main() {
    val host = "localhost"
    val user = "root"
    val password = "insecure"
    val prototypeLifecycle = JdbcConnectionLifecycle(host, user, password, "prototype")

    prototypeLifecycle.withConnection { connection ->
        fun dropTable(table: Table) {
            val sql = "drop table if exists ${table.name}"
            val statement = connection.prepareStatement(sql)
            val result = statement.execute()
            println("$result $sql")
        }
        Schema.tables.reversed().map(::dropTable)
    }
}
