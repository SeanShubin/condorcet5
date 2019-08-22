package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.ConnectionWrapper

class SchemaInitializer(private val connection: ConnectionWrapper,
                        private val schemaName: String) : Initializer {
    override fun initialize() {
        if (needsInitialize()) {
            createDatabase()
            useDatabase()
            createSchema()
            createStaticData()
        } else {
            useDatabase()
        }
    }

    private fun needsInitialize(): Boolean {
        val hasSchema = "select count(*) from information_schema.schemata where schema_name = ?"
        return connection.queryExactlyOneInt(hasSchema, schemaName) == 0
    }

    private fun createDatabase() {
        connection.update("create database $schemaName")
    }

    private fun useDatabase() {
        connection.update("use $schemaName")
    }

    private fun createSchema() {
        val createTableStatements = Schema.tables.flatMap { it.toCreateTableStatements() }
        createTableStatements.forEach {
            connection.update(it)
        }
    }

    private fun createStaticData() {
        DbStatus.values().forEach {
            connection.update("insert into status (name) values (?)", it.name.toLowerCase())
        }
    }
}
