package com.seanshubin.condorcet.prototype

import java.sql.ResultSet
import java.sql.Timestamp
import java.time.Instant

fun main() {
    val lifecycle = LocalConnectionLifecycle
    lifecycle.withConnection { connection ->
        fun execSql(sql: String, vararg arg: Any?) {
            try {
                val statement = connection.prepareStatement(sql)
                for (indexedValue in arg.withIndex()) {
                    val (index, value) = indexedValue
                    val position = index + 1
                    statement.setObject(position, value)
                }
                statement.execute()
            } catch (exception: Exception) {
                println(sql)
                println(exception.message)
                exception.printStackTrace()
            }
        }

        fun execQuery(sql: String, vararg arg: Any?): ResultSet {
            try {
                val statement = connection.prepareStatement(sql)
                for (indexedValue in arg.withIndex()) {
                    val (index, value) = indexedValue
                    val position = index + 1
                    statement.setObject(position, value)
                }
                return statement.executeQuery()
            } catch (exception: Exception) {
                println(sql)
                throw exception
            }
        }

        val theTime = Timestamp.from(Instant.parse("2019-07-01T01:30:54.867627Z"))
        execSql("drop table if exists foo")
        execSql("""create table foo (
                  |    the_time datetime(6)
                  |);""".trimMargin())
        execSql("insert into foo (the_time) values (?)", theTime)
        val resultSet = execQuery("select * from foo")
        while (resultSet.next()) {
            val timestamp = resultSet.getTimestamp(1)
            val newInstant = timestamp.toInstant()
            println(newInstant)
        }


    }
}