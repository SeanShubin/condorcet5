package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.util.db.ConnectionWrapper
import java.sql.DriverManager

object ConnectionFactory {
    fun <T> withConnection(host: String,
                           user: String,
                           password: String,
                           sqlEvent: (String) -> Unit,
                           f: (ConnectionWrapper) -> T): T {
        val url = "jdbc:mysql://$host?serverTimezone=UTC"
        val connection = DriverManager.getConnection(url, user, password)
        return connection.use {
            val connectionWrapper = ConnectionWrapper(connection, sqlEvent)
            return f(connectionWrapper)
        }

    }

    fun <T> withConnection(host: String,
                           user: String,
                           password: String,
                           f: (ConnectionWrapper) -> T): T {
        val url = "jdbc:mysql://$host?serverTimezone=UTC"
        val connection = DriverManager.getConnection(url, user, password)
        return connection.use {
            val connectionWrapper = ConnectionWrapper(connection) {}
            return f(connectionWrapper)
        }
    }
}
