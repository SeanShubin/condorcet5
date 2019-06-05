package com.seanshubin.condorcet.rdsutil

import com.amazonaws.services.rds.AmazonRDSAsync
import com.amazonaws.services.rds.AmazonRDSAsyncClientBuilder
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RdsDatabaseApiImplIntegrationTest {
    @Test
    fun createDatabase() {
        // given
        val databaseName = "sean-test-mysql-db"
        val rdsClient: AmazonRDSAsync =
                AmazonRDSAsyncClientBuilder.standard().withCredentials(TestCredentialsProvider).build()
        val api: RdsDatabaseApi = RdsDatabaseApiImpl(rdsClient)

        // when-then
        assertFalse(api.databaseExists(databaseName))
        api.createDatabase(databaseName, "master-password")
        assertTrue(api.databaseExists(databaseName))
        api.deleteDatabase(databaseName)
        api.waitForDatabaseToGoAway(databaseName)
        assertFalse(api.databaseExists(databaseName))
    }

}