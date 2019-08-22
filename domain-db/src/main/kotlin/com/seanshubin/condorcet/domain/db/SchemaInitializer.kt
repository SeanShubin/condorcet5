package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.ConnectionProvider

class SchemaInitializer(private val connectionProvider: ConnectionProvider,
                        private val schemaName: String) : Initializer {
    override fun initialize() {
        if (needsInitialize()) {
            createDatabase()
            createSchema()
            createStaticData()
        }
    }

    private fun needsInitialize(): Boolean {
        val connection = connectionProvider.getConnection()
        val hasSchema = "select count(*) from information_schema.schemata where schema_name = ?;"
        return connection.queryExactlyOneInt(hasSchema, schemaName) == 0
    }

    private fun createDatabase() {
        val connection = connectionProvider.getConnection()
        connection.update("create database ?", schemaName)
        connection.update("use ?", schemaName)
    }

    private fun createSchema() {
        val connection = connectionProvider.getConnection()
        val createTableStatements = Schema.tables.flatMap { it.toCreateTableStatements() }
        createTableStatements.forEach {
            connection.update(it)
        }
    }

    private fun createStaticData() {
        val connection = connectionProvider.getConnection()
        val statusValues = listOf("editing", "live", "complete")
        statusValues.forEach {
            connection.update("insert into status (name) values (?)", it)
        }
    }
}
