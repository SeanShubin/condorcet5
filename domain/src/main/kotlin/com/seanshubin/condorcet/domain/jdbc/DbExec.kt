package com.seanshubin.condorcet.domain.jdbc

import java.sql.ResultSet

interface DbExec {
    fun query(sql: String, vararg args: Any?): ResultSet
    fun update(sql: String, vararg args: Any?)
}
