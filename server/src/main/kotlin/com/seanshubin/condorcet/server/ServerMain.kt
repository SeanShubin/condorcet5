package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.domain.ApiBackedByDb
import com.seanshubin.condorcet.domain.db.*
import com.seanshubin.condorcet.json.api.JsonApi
import com.seanshubin.condorcet.logger.LoggerFactory
import com.seanshubin.condorcet.util.ClassLoaderUtil
import java.nio.file.Paths
import java.time.Clock
import java.util.*

fun main(args: Array<String>) {
    val logDir = LoggerFactory.createLogGroup(Paths.get("out", "log", "foo"))
    val sqlLogger = logDir.create("sql")
    fun sqlEvent(sql: String) = sqlLogger.log("${sql.trim()};")
    val host = "localhost"
    val user = "root"
    val password = "insecure"
    ConnectionFactory.withConnection(host, user, password, ::sqlEvent) { connection ->
        fun loadResource(name: String): String = ClassLoaderUtil.loadResourceAsString("sql/$name")
        val dbFromResource = DbFromResourceImpl(
                connection,
                ::loadResource
        )

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
        val createWithPort = JettyServerFactory::createWithPort
        val applicationRunner = ApplicationRunner(port, handler, createWithPort);
        applicationRunner.run()
    }
}
