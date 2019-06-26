package com.seanshubin.condorcet.util.db

import java.sql.Connection
import java.sql.ResultSet

interface ConnectionLifecycle {
    fun <T> withConnection(doSomethingWithConnection: (Connection) -> T): T
    fun <T> withResultSet(sqlQuery: String, doSomethingWithResultSet: (ResultSet) -> T): T
}
