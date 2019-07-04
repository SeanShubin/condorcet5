package com.seanshubin.condorcet.util.db

import com.mysql.cj.jdbc.ClientPreparedStatement
import java.sql.Connection
import java.sql.ResultSet

class ConnectionWrapper(private val connection: Connection,
                        private val sqlEvent: (String) -> Unit) {
    fun <T> execQuery(sql: String, f: (ResultSet) -> T): T {
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        return statement.use {
            sqlEvent(statement.asSql())
            f(statement.executeQuery())
        }
    }

    fun execUpdate(sql: String): Int {
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        return statement.use {
            sqlEvent(statement.asSql())
            statement.executeUpdate()
        }
    }
}
