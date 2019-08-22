package com.seanshubin.condorcet.domain.db

import java.sql.ResultSet

interface DbFromResource {
    fun <T> queryExactlyOneRow(createFunction: (ResultSet) -> T,
                               sqlResource: String,
                               vararg parameters: Any?): T

    fun <T> queryZeroOrOneRow(createFunction: (ResultSet) -> T,
                              sqlResource: String,
                              vararg parameters: Any?): T?

    fun <T> query(createFunction: (ResultSet) -> T,
                  sqlResource: String,
                  vararg parameters: Any?): List<T>

    fun queryExactlyOneInt(sqlResource: String, vararg parameters: Any?): Int
    fun queryZeroOrOneInt(sqlResource: String, vararg parameters: Any?): Int?
    fun update(sqlResource: String, vararg parameters: Any?): Int
}
