package com.seanshubin.condorcet.util.db.jdbc

import java.sql.Connection

class ConnectionDbFunctions(private val connection: Connection) : DbFunctions {
    override fun <T> inTransaction(f: () -> T): T {
        connection.autoCommit = false
        try {
            val result = f()
            connection.commit()
            return result
        } catch (ex: Exception) {
            connection.rollback()
            throw ex
        } finally {
            connection.autoCommit = true
        }
    }

    override fun <T> inTransactionAlwaysRollback(f: () -> T): T {
        connection.autoCommit = false
        try {
            return f()
        } finally {
            connection.rollback()
            connection.autoCommit = true
        }
    }
}
