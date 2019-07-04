package com.seanshubin.condorcet.util.db

import java.sql.DriverManager

object ConnectionFactory {
    fun <T> withConnection(info: ConnectionInfo,
                           sqlEvent: (String) -> Unit,
                           f: (ConnectionWrapper) -> T): T {
        val (host, user, password) = info
        val url = "jdbc:mysql://$host?serverTimezone=UTC"
        val connection = DriverManager.getConnection(url, user, password)
        return connection.use {
            val connectionWrapper = ConnectionWrapper(connection, sqlEvent)
            return f(connectionWrapper)
        }
    }

    fun <T> withConnection(info: ConnectionInfo,
                           f: (ConnectionWrapper) -> T): T {
        return withConnection(info, {}, f)
    }
}
