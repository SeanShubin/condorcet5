package com.seanshubin.condorcet.prototype

import com.amazonaws.services.rds.AmazonRDSAsync
import com.amazonaws.services.rds.AmazonRDSAsyncClientBuilder
import com.amazonaws.services.rds.model.CreateDBInstanceRequest
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.Duration

fun main() {
    val rdsClient: AmazonRDSAsync =
            AmazonRDSAsyncClientBuilder
                    .standard()
                    .build()
    val instanceIdentifier = "wait-for-provision-prototype"
    val createDbInstanceRequest = CreateDBInstanceRequest()
            .withDBInstanceIdentifier(instanceIdentifier)
            .withEngine("mysql")
            .withDBInstanceClass("db.t2.micro")
            .withMasterUsername("dbuser")
            .withMasterUserPassword("dbpassword")
            .withDBName("dbname")
            .withAllocatedStorage(20)
    rdsClient.createDBInstance(createDbInstanceRequest)
    fun getStatus(): String {
        val describeDbInstancesRequest = DescribeDBInstancesRequest()
                .withDBInstanceIdentifier(instanceIdentifier)
        val describeDbInstancesResponse =
                rdsClient.describeDBInstances(describeDbInstancesRequest)
        assert(describeDbInstancesResponse.dbInstances.size == 1)
        val dbInstance = describeDbInstancesResponse.dbInstances[0]
        return dbInstance.dbInstanceStatus
    }

    val fiveSeconds = Duration.ofSeconds(5).toMillis()
    val startedMillis = System.currentTimeMillis()
    fun timePassedString(): String {
        val totalMillisPassed = System.currentTimeMillis() - startedMillis
        val totalSecondsPassed = totalMillisPassed / 1000
        val secondsPassed = totalSecondsPassed % 60
        val totalMinutesPassed = totalSecondsPassed / 60
        return if (totalMinutesPassed == 0L) {
            "$secondsPassed seconds"
        } else {
            "$totalMinutesPassed minutes, $secondsPassed seconds"
        }
    }
    runBlocking {
        var done = false
        while (!done) {
            val status = getStatus()
            if (status == "available") {
                done = true
            } else {
                delay(fiveSeconds)
            }
            println("$status ${timePassedString()}")
        }
    }
    println("Database instance $instanceIdentifier is available")
}
