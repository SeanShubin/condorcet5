package com.seanshubin.condorcet.rdsutil

import com.amazonaws.services.rds.AmazonRDSAsync
import com.amazonaws.services.rds.AmazonRDSAsyncClientBuilder
import com.seanshubin.condorcet.util.DurationFormat
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RdsDatabaseApiImplIntegrationTest {
    @Test
    fun createDatabase() {
        // given
        val databaseName = "sean-test-mysql-db"
        val api: RdsDatabaseApi = createApi()

        // when-then
//        assertFalse(api.databaseExists(databaseName))
//        timing("api.createDatabase($databaseName)") {
//            api.createDatabase(databaseName)
//        }
        assertTrue(api.databaseExists(databaseName))
        timing("api.waitForDatabaseToBeAvailable($databaseName)") {
            api.waitForDatabaseToBeAvailable(databaseName)
        }
        timing("api.deleteDatabase($databaseName)") {
            api.deleteDatabase(databaseName)
        }
        timing("api.waitForDatabaseToGoAway($databaseName)") {
            api.waitForDatabaseToGoAway(databaseName)
        }
        assertFalse(api.databaseExists(databaseName))
    }

    fun createApi(): RdsDatabaseApi {
        val properties = Properties()
        val path = Paths.get("/Keybase/private/seanshubin/credentials")
        val inputStream = Files.newInputStream(path)
        properties.load(inputStream)
        val credentialsProvider = PropertiesCredentialsProvider(properties)
        val rdsClient: AmazonRDSAsync =
                AmazonRDSAsyncClientBuilder.standard().withCredentials(credentialsProvider).build()
        val api: RdsDatabaseApi = RdsDatabaseApiImpl(rdsClient)
        return api
    }

    fun timing(caption: String, f: () -> Unit) {
        val start = System.currentTimeMillis()
        f()
        val end = System.currentTimeMillis()
        val duration = end - start
        val durationString = DurationFormat.milliseconds.format(duration)
        println("$caption: $durationString")
    }
}
