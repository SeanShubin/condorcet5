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
    private val logDir = Paths.get("out", "log", "foo")
    private val logGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    private val sqlLogger = logGroup.create("sql")
    private val sqlEvent = LogDecorators.logSql(sqlLogger)
    private val host = "localhost"
    private val user = "root"
    private val password = "insecure"
    private val connection = ConnectionFactory.createConnection(host, user, password, sqlEvent)
    private val loadResource = ClassLoaderUtil.loadResourceRelativeFunction("sql")
    private val dbFromResource = DbFromResourceImpl(connection, loadResource)
    private val dbQuery = ResourceDbApiQueries(dbFromResource)
    private val resourceDbCommands = ResourceDbApiCommands(dbFromResource)
    private val eventHandler: EventHandler = DbEventHandler(resourceDbCommands)
    private val eventDbQueries = ResourceEventDbQueries(dbFromResource)
    private val eventDbCommands = ResourceEventDbCommands(dbFromResource)
    private val synchronizer: Synchronizer = EventSynchronizer(eventDbQueries, eventDbCommands, eventHandler)
    private val clock = Clock.systemUTC()
    private val dbCommand = DbApiCommandsWithEvents(dbFromResource, clock, synchronizer)
    private val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
    private val oneWayHash: OneWayHash = Sha256Hash()
    private val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    private val port = 4000
    private val seed = 12345L
    private val random = Random(seed)
    private val api = ApiBackedByDb(dbQuery, dbCommand, clock, passwordUtil, uniqueIdGenerator, random)
    private val jsonApi = JsonApi(api)
    private val valueHandler = CondorcetHandler(jsonApi)
    private val handler = ValueHandlerToJettyHandlerAdapter(valueHandler)
    private val createJetty = JettyServerFactory.createFunction(port)
    val applicationRunner = ApplicationRunner(handler, createJetty)
}
