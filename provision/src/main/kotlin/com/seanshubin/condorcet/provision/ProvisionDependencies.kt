package com.seanshubin.condorcet.provision

import com.seanshubin.condorcet.domain.db.Initializer
import com.seanshubin.condorcet.domain.db.SchemaInitializerFactory
import com.seanshubin.condorcet.logger.LogDecorators
import com.seanshubin.condorcet.logger.LogGroup
import com.seanshubin.condorcet.logger.Logger
import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ConnectionWrapper
import java.nio.file.Path
import java.nio.file.Paths

class ProvisionDependencies {
    private val logDir: Path = Paths.get("out", "log", "provision")
    private val logGroup: LogGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    private val sqlLogger: Logger = logGroup.create("sql")
    private val sqlEvent: (String) -> Unit = LogDecorators.logSql(sqlLogger)
    private val host: String = "localhost"
    private val user: String = "root"
    private val password: String = "insecure"
    private val schemaName: String = "condorcet"
    private val connection: ConnectionWrapper = ConnectionFactory.createConnection(host, user, password, sqlEvent)
    private val initializer: Initializer = SchemaInitializerFactory.create(connection, schemaName)
    val provisionSetup: Runnable = ProvisionSetup(initializer)
}
