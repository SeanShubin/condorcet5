package com.seanshubin.condorcet.util.db

import java.sql.DriverManager

object ConnectionFactory {
    fun <T> withConnection(info: ConnectionInfo,
                           sqlEvent: (String) -> Unit,
                           f: (ConnectionWrapper) -> T): T {
        val connectionWrapper = createConnection(info, sqlEvent)
        return connectionWrapper.use {
            return f(connectionWrapper)
        }
    }

    fun <T> withConnection(info: ConnectionInfo,
                           f: (ConnectionWrapper) -> T): T {
        return withConnection(info, {}, f)
    }

    fun createConnection(info: ConnectionInfo,
                         sqlEvent: (String) -> Unit): ConnectionWrapper {
        val (host, user, password) = info
        return createConnection(host, user, password, sqlEvent)
    }

    fun createConnection(host: String,
                         user: String,
                         password: String,
                         sqlEvent: (String) -> Unit): ConnectionWrapper {
        val url = "jdbc:mysql://$host?serverTimezone=UTC"
        val connection = DriverManager.getConnection(url, user, password)
        return ConnectionWrapper(connection, sqlEvent)
    }
}
