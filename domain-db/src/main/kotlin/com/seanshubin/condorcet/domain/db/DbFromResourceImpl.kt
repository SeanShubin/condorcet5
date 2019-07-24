package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.ConnectionWrapper
import java.sql.ResultSet

class DbFromResourceImpl(private val connection: ConnectionWrapper,
                         private val loadResource: (String) -> String) : DbFromResource {
    override fun <T> queryExactlyOneRow(createFunction: (ResultSet) -> T,
                                        sqlResource: String,
                                        vararg parameters: Any?): T {
        val sql = loadResource(sqlResource)
        return connection.execQuery(sql, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = createFunction(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$sql'")
                }
                result
            } else {
                throw RuntimeException("Exactly 1 row expected for '$sql', got none")
            }
        }
    }

    override fun <T> queryZeroOrOneRow(createFunction: (ResultSet) -> T,
                                       sqlResource: String,
                                       vararg parameters: Any?): T? {
        val sql = loadResource(sqlResource)
        return connection.execQuery(sql, *parameters) { resultSet ->
            if (resultSet.next()) {
                val result = createFunction(resultSet)
                if (resultSet.next()) {
                    throw RuntimeException("No more than 1 row expected for '$sql'")
                }
                result
            } else {
                null
            }
        }
    }

    override fun <T> query(createFunction: (ResultSet) -> T,
                           sqlResource: String,
                           vararg parameters: Any?): List<T> {
        val sql = loadResource(sqlResource)
        return connection.execQuery(sql, *parameters) { resultSet ->
            val results = mutableListOf<T>()
            while (resultSet.next()) {
                results.add(createFunction(resultSet))
            }
            results
        }
    }

    override fun queryExactlyOneInt(sqlResource: String, vararg parameters: Any?): Int =
            queryExactlyOneRow(::createInt, sqlResource, *parameters)

    override fun queryZeroOrOneInt(sqlResource: String, vararg parameters: Any?): Int? =
            queryZeroOrOneRow(::createInt, sqlResource, *parameters)

    override fun update(sqlResource: String, vararg parameters: Any?): Int {
        val sql = loadResource(sqlResource)
        return connection.execUpdate(sql, *parameters)
    }
}