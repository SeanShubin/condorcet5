package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.ConnectionWrapper
import com.seanshubin.condorcet.util.db.ConnectionWrapper.Companion.createInt
import java.sql.ResultSet

class DbFromResourceImpl(private val connection: ConnectionWrapper,
                         private val loadResource: (String) -> String) : DbFromResource {
    override fun <T> queryExactlyOneRow(createFunction: (ResultSet) -> T,
                                        sqlResource: String,
                                        vararg parameters: Any?): T {
        val sql = loadResource(sqlResource)
        return connection.queryExactlyOneRow(sql, *parameters) { createFunction(it) }
    }

    override fun <T> queryZeroOrOneRow(createFunction: (ResultSet) -> T,
                                       sqlResource: String,
                                       vararg parameters: Any?): T? {
        val sql = loadResource(sqlResource)
        return connection.queryZeroOrOneRow(sql, *parameters) { createFunction(it) }
    }

    override fun <T> query(createFunction: (ResultSet) -> T,
                           sqlResource: String,
                           vararg parameters: Any?): List<T> {
        val sql = loadResource(sqlResource)
        return connection.queryList(sql, *parameters) { createFunction(it) }
    }

    override fun queryExactlyOneInt(sqlResource: String, vararg parameters: Any?): Int =
            queryExactlyOneRow(::createInt, sqlResource, *parameters)

    override fun queryZeroOrOneInt(sqlResource: String, vararg parameters: Any?): Int? =
            queryZeroOrOneRow(::createInt, sqlResource, *parameters)

    override fun update(sqlResource: String, vararg parameters: Any?): Int {
        val sql = loadResource(sqlResource)
        return connection.update(sql, *parameters)
    }
}
