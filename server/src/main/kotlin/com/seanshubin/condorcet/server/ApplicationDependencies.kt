package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.domain.ApiBackedByDb
import com.seanshubin.condorcet.domain.db.*
import com.seanshubin.condorcet.json.api.JsonApi
import com.seanshubin.condorcet.json.api.JsonCommandApi
import com.seanshubin.condorcet.logger.LogDecorators
import com.seanshubin.condorcet.logger.LogGroup
import com.seanshubin.condorcet.logger.Logger
import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.util.ClassLoaderUtil
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ConnectionWrapper
import org.eclipse.jetty.server.Handler
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Clock
import java.util.*

class ApplicationDependencies(args: Array<String>) {
    private val logDir: Path = Paths.get("out", "log", "foo")
    private val logGroup: LogGroup = LoggerFactory.instanceDefaultZone.createLogGroup(logDir)
    private val sqlLogger: Logger = logGroup.create("sql")
    private val sqlEvent: (String) -> Unit = LogDecorators.logSql(sqlLogger)
    private val host: String = "localhost"
    private val user: String = "root"
    private val password: String = "insecure"
    private val schemaName: String = "condorcet"
    private val connection: ConnectionWrapper = ConnectionFactory.createConnection(host, user, password, sqlEvent)
    private val loadResource: (String) -> String = ClassLoaderUtil.loadResourceRelativeFunction("sql")
    private val dbFromResource: DbFromResource = DbFromResourceImpl(connection, loadResource)
    private val dbQuery: MutableDbQueries = ResourceDbApiQueries(dbFromResource)
    private val resourceDbCommands: MutableDbCommands = ResourceDbApiCommands(dbFromResource)
    private val eventHandler: EventHandler = DbEventHandler(resourceDbCommands)
    private val eventDbQueries: EventDbQueries = ResourceEventDbQueries(dbFromResource)
    private val eventDbCommands: EventDbCommands = ResourceEventDbCommands(dbFromResource)
    private val synchronizer: Synchronizer = EventSynchronizer(eventDbQueries, eventDbCommands, eventHandler)
    private val clock: Clock = Clock.systemUTC()
    private val dbCommand: MutableDbCommands = DbApiCommandsWithEvents(dbFromResource, clock, synchronizer)
    private val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
    private val oneWayHash: OneWayHash = Sha256Hash()
    private val passwordUtil: PasswordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    private val port: Int = 4000
    private val seed: Long = 12345L
    private val random: Random = Random(seed)
    private val api: Api = ApiBackedByDb(dbQuery, dbCommand, clock, passwordUtil, uniqueIdGenerator, random)
    private val jsonApi: JsonApi = JsonCommandApi(api)
    private val valueHandler: ValueHandler = CondorcetHandler(jsonApi)
    private val handler: Handler = ValueHandlerToJettyHandlerAdapter(valueHandler)
    private val createJetty: () -> JettyServerContract = JettyServerFactory.createFunction(port)
    private val initializer: Initializer = SchemaInitializerFactory.create(connection, schemaName)
    val applicationRunner: Runnable = ApplicationRunner(initializer, handler, createJetty)
}
