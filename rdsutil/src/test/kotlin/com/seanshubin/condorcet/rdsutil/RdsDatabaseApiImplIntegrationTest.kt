package com.seanshubin.condorcet.rdsutil

import com.amazonaws.services.rds.AmazonRDSAsync
import com.amazonaws.services.rds.AmazonRDSAsyncClientBuilder
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.test.Ignore
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RdsDatabaseApiImplIntegrationTest {
    @Ignore
    fun createDatabase() {
        // given
        val databaseName = "sean-test-mysql-db"
        val api: RdsDatabaseApi = createApi()

        // when-then
        assertFalse(api.databaseExists(databaseName))
        api.createDatabase(databaseName)
        assertTrue(api.databaseExists(databaseName))
        api.waitForDatabaseToBeAvailable(databaseName)
        api.deleteDatabase(databaseName)
        api.waitForDatabaseToGoAway(databaseName)
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
}
