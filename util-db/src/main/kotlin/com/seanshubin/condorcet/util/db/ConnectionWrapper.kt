package com.seanshubin.condorcet.util.db

import com.mysql.cj.jdbc.ClientPreparedStatement
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant

class ConnectionWrapper(private val connection: Connection,
                        private val sqlEvent: (String) -> Unit) {
    fun <T> execQuery(sql: String, vararg parameters: Any?, f: (ResultSet) -> T): T {
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        updateParameters(parameters, statement)
        return statement.use {
            sqlEvent(statement.asSql())
            f(statement.executeQuery())
        }
    }


    fun execUpdate(sql: String, vararg parameters: Any?): Int {
        val statement = connection.prepareStatement(sql) as ClientPreparedStatement
        updateParameters(parameters, statement)
        return statement.use {
            sqlEvent(statement.asSql())
            statement.executeUpdate()
        }
    }

    private fun updateParameters(parameters: Array<out Any?>, statement: ClientPreparedStatement) {
        parameters.toList().forEachIndexed { index, any ->
            val position = index + 1
            if (any == null) {
                statement.setObject(position, null)
            } else when (any) {
                is String -> statement.setString(position, any)
                is Boolean -> statement.setBoolean(position, any)
                is Int -> statement.setInt(position, any)
                is Instant -> statement.setTimestamp(position, Timestamp.from(any))
                else -> throw UnsupportedOperationException("Unsupported type ${any.javaClass.simpleName}")
            }
        }
    }
}
