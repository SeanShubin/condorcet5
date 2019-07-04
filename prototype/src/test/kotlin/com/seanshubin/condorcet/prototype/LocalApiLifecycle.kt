package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.domain.ApiBackedByDb
import com.seanshubin.condorcet.domain.PrepareStatementApi
import com.seanshubin.condorcet.util.ClassLoaderUtil
import com.seanshubin.condorcet.util.db.JdbcConnectionLifecycle
import com.seanshubin.condorcet.util.db.jdbc.LoggingPreparedStatement
import java.sql.PreparedStatement
import java.time.Clock
import java.util.*

object LocalApiLifecycle : ApiLifecycle {
    override fun <T> withApi(f: (Api) -> T): T {
        val host = "localhost"
        val user = "root"
        val password = "insecure"
        val database = "prototype"
        val lifecycle = JdbcConnectionLifecycle(host, user, password, database)
        return lifecycle.withConnection { connection ->
            val emitLine: (String) -> Unit = ::println
            fun prepareStatement(sql: String): PreparedStatement =
                    LoggingPreparedStatement(sql, connection.prepareStatement(sql), emitLine)

            val db = PrepareStatementApi(
                    ::prepareStatement,
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
}
