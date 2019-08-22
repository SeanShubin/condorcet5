package com.seanshubin.condorcet.domain.db

import com.seanshubin.condorcet.util.db.ConnectionWrapper

object SchemaInitializerFactory {
    fun create(connection: ConnectionWrapper, schemaName: String): SchemaInitializer = SchemaInitializer(connection, schemaName)
}
