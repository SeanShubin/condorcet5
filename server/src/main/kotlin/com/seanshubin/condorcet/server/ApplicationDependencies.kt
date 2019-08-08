package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.domain.ApiBackedByDb
import com.seanshubin.condorcet.domain.db.*
import com.seanshubin.condorcet.json.api.JsonApi
import com.seanshubin.condorcet.logger.LogDecorators
import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.util.ClassLoaderUtil
import com.seanshubin.condorcet.util.db.ConnectionFactory
import java.nio.file.Paths
import java.time.Clock
import java.util.*

class ApplicationDependencies(args: Array<String>) {
    val logDir = Paths.get("out", "log", "foo")
    val logGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    val sqlLogger = logGroup.create("sql")
    val sqlEvent = LogDecorators.logSql(sqlLogger)
    val host = "localhost"
    val user = "root"
    val password = "insecure"
    val connection = ConnectionFactory.createConnection(host, user, password, sqlEvent)
    val loadResource = ClassLoaderUtil.loadResourceRelativeFunction("sql")
    val dbFromResource = DbFromResourceImpl(connection, loadResource)
    val dbQuery = ResourceDbApiQueries(dbFromResource)
    val resourceDbCommands = ResourceDbApiCommands(dbFromResource)
    val eventHandler: EventHandler = DbEventHandler(resourceDbCommands)
    val eventDbQueries = ResourceEventDbQueries(dbFromResource)
    val eventDbCommands = ResourceEventDbCommands(dbFromResource)
    val synchronizer: Synchronizer = EventSynchronizer(eventDbQueries, eventDbCommands, eventHandler)
    val clock = Clock.systemUTC()
    val dbCommand = DbApiCommandsWithEvents(dbFromResource, clock, synchronizer)
    val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
    val oneWayHash: OneWayHash = Sha256Hash()
    val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    val port = 4000
    val seed = 12345L
    val random = Random(seed)
    val api = ApiBackedByDb(dbQuery, dbCommand, clock, passwordUtil, uniqueIdGenerator, random)
    val jsonApi = JsonApi(api)
    val handler = CondorcetHandler(jsonApi)
    val createJetty = JettyServerFactory.createFunction(port)
    val applicationRunner = ApplicationRunner(handler, createJetty)
}
