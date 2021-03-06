package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.domain.ApiBackedByDb
import com.seanshubin.condorcet.domain.db.*
import com.seanshubin.condorcet.logger.Logger
import com.seanshubin.condorcet.util.ClassLoaderUtil
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ConnectionWrapper
import java.time.Clock
import java.util.*

object ApiFactory {
    fun createApi(connection: ConnectionWrapper,
                  clock: Clock,
                  uniqueIdGenerator: UniqueIdGenerator): Api {
        fun loadResource(name: String): String = ClassLoaderUtil.loadResourceAsString("sql/$name")
        val dbFromResource = DbFromResourceImpl(
                connection,
                ::loadResource
        )
        val dbQueries = createMutableDbQueries(dbFromResource)
        val dbCommands = createMutableDbCommands(dbFromResource, clock)
        val oneWayHash: OneWayHash = Sha256Hash()
        val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        val seed = 12345L
        val random = Random(seed)
        val api = ApiBackedByDb(dbQueries, dbCommands, clock, passwordUtil, uniqueIdGenerator, random)
        return api
    }

    fun <T> withApi(connection: ConnectionWrapper,
                    clock: Clock,
                    uniqueIdGenerator: UniqueIdGenerator,
                    f: (Api) -> T): T {
        val api = createApi(connection, clock, uniqueIdGenerator)
        return f(api)
    }

    fun <T> withApi(logger: Logger, f: (Api) -> T): T {
        val emit: (String) -> Unit = logger::log
        fun sqlEvent(sql: String): Unit = emit(sql)
        return ConnectionFactory.withConnection(
                Connections.local,
                ::sqlEvent) { connection ->
            val clock = Clock.systemDefaultZone()
            val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
            withApi(connection, clock, uniqueIdGenerator, f)
        }
    }

    fun <T> withApiAndCleanDatabase(connection: ConnectionWrapper,
                                    clock: Clock,
                                    uniqueIdGenerator: UniqueIdGenerator,
                                    f: (Api) -> T): T {
        return withApi(connection, clock, uniqueIdGenerator) { api ->
            fun execUpdate(sql: String) {
                connection.update(sql)
            }
            connection.update("create database if not exists sample")
            connection.update("use sample")
            SampleData.dropTables().forEach(::execUpdate)
            SampleData.createTables().forEach(::execUpdate)
            SampleData.staticData().forEach(::execUpdate)
            f(api)
        }
    }

    private fun createMutableDbCommands(dbFromResource: DbFromResource, clock: Clock): MutableDbCommands {
        val resourceDbCommands = ResourceDbApiCommands(dbFromResource)
        val eventHandler: EventHandler = DbEventHandler(resourceDbCommands)
        val eventDbQueries = ResourceEventDbQueries(dbFromResource)
        val eventDbCommands = ResourceEventDbCommands(dbFromResource)
        val synchronizer: Synchronizer = EventSynchronizer(eventDbQueries, eventDbCommands, eventHandler)
        val dbApiCommandsWithEvents = DbApiCommandsWithEvents(dbFromResource, clock, synchronizer)
        return dbApiCommandsWithEvents
    }

    private fun createMutableDbQueries(dbFromResource: DbFromResource): MutableDbQueries {
        val resourceDbQueries = ResourceDbApiQueries(dbFromResource)
        return resourceDbQueries
    }
}
