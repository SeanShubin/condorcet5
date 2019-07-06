package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.domain.ApiBackedByDb
import com.seanshubin.condorcet.domain.PrepareStatementApi
import com.seanshubin.condorcet.logger.Logger
import com.seanshubin.condorcet.util.ClassLoaderUtil
import com.seanshubin.condorcet.util.db.ConnectionFactory
import com.seanshubin.condorcet.util.db.ConnectionWrapper
import java.time.Clock
import java.util.*

object ApiFactory {
    fun <T> withApi(connection: ConnectionWrapper, clock: Clock, f: (Api) -> T): T {
        val db = PrepareStatementApi(
                connection,
                ClassLoaderUtil::loadResourceAsString)
        val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
        val oneWayHash: OneWayHash = Sha256Hash()
        val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
        val seed = 12345L
        val random = Random(seed)
        val api = ApiBackedByDb(db, clock, passwordUtil, uniqueIdGenerator, random)
        return f(api)
    }

    fun <T> withApi(logger: Logger, f: (Api) -> T): T {
        val emit: (String) -> Unit = logger::log
        fun sqlEvent(sql: String): Unit = emit(sql)
        return ConnectionFactory.withConnection(
                Connections.local,
                ::sqlEvent) { connection ->
            val db = PrepareStatementApi(
                    connection,
                    ClassLoaderUtil::loadResourceAsString)
            val clock = Clock.systemDefaultZone()
            val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
            val oneWayHash: OneWayHash = Sha256Hash()
            val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
            val seed = 12345L
            val random = Random(seed)
            val api = ApiBackedByDb(db, clock, passwordUtil, uniqueIdGenerator, random)
            f(api)
        }
    }

    fun <T> withApiAndCleanDatabase(connection: ConnectionWrapper, clock: Clock, f: (Api) -> T): T {
        return withApi(connection, clock) { api ->
            fun execUpdate(sql: String) {
                connection.execUpdate(sql)
            }
            connection.execUpdate("create database if not exists sample")
            connection.execUpdate("use sample")
            SampleData.dropTables().forEach(::execUpdate)
            SampleData.createTables().forEach(::execUpdate)
            SampleData.staticData().forEach(::execUpdate)
            f(api)
        }
    }
}
