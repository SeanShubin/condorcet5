package com.seanshubin.condorcet.jdbc

import java.sql.ResultSet

interface DbExec {
    fun query(sql: String, vararg args: Any?): ResultSet
    fun update(sql: String, vararg args: Any?)
}
