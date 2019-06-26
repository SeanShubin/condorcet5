package com.seanshubin.condorcet.util.db

import java.sql.ResultSet

interface ConnectionLifecycle {
    fun <T> withResultSet(sqlQuery: String, doSomethingWithResultSet: (ResultSet) -> T): T
}
