package com.seanshubin.condorcet.rdsutil

import com.amazonaws.services.rds.AmazonRDSAsync
import com.amazonaws.services.rds.model.CreateDBInstanceRequest
import com.amazonaws.services.rds.model.DBInstance
import com.amazonaws.services.rds.model.DeleteDBInstanceRequest
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest
import com.seanshubin.condorcet.util.retryDuration
import java.time.Duration

class RdsDatabaseApiImpl(private val rdsClient: AmazonRDSAsync) : RdsDatabaseApi {
    override fun createInstance(instanceIdentifier: String) {
        val createDbInstanceRequest = CreateDBInstanceRequest()
                .withDBInstanceIdentifier(instanceIdentifier)
                .withEngine("mysql")
                .withDBInstanceClass("db.t2.micro")
                .withMasterUsername("masterusername")
                .withMasterUserPassword("masteruserpassword")
                .withAllocatedStorage(20).withDBName("dbname")
        rdsClient.createDBInstance(createDbInstanceRequest)
    }

    override fun instanceExists(instanceIdentifier: String): Boolean {
        val describeDbInstancesRequest = DescribeDBInstancesRequest()
        val describeDbInstancesResponse = rdsClient.describeDBInstances(describeDbInstancesRequest)
        val dbInstances = describeDbInstancesResponse.dbInstances
        for (dbInstance in dbInstances) {
            println(dbInstance.dbInstanceIdentifier)
            println(dbInstance.dbName)
            println(dbInstance.dbInstanceStatus)
            val endpoint = dbInstance.endpoint
            if (endpoint != null) {
                println(endpoint.address)
                println(endpoint.hostedZoneId)
                println(endpoint.port)
            }
            println()
        }
        return dbInstances.any { dbInstance -> dbInstance.dbInstanceIdentifier == instanceIdentifier }
    }

    override fun deleteInstance(instanceIdentifier: String) {
        val deleteDbInstanceRequest = DeleteDBInstanceRequest().withDBInstanceIdentifier(instanceIdentifier).withSkipFinalSnapshot(true)
        rdsClient.deleteDBInstanceAsync(deleteDbInstanceRequest).get()
    }

    override fun waitForInstanceToGoAway(instanceIdentifier: String) {
        retryDuration(howOftenToCheck, howLongToWait) {
            !instanceExists(instanceIdentifier)
        }
    }

    override fun waitForInstanceToBeAvailable(instanceIdentifier: String) {
        var x = 0
        waitForDbInstance(instanceIdentifier) { instance ->
            x++
            val status = instance.dbInstanceStatus
            val endpoint = instance.endpoint
            println("$x $status $endpoint")
            instance.dbInstanceStatus == "available"
        }
    }

    override fun instanceStatus(instanceIdentifier: String): String =
            withDbInstance(instanceIdentifier) { instance ->
                instance.dbInstanceStatus
            }

    private fun findInstanceByName(instanceIdentifier: String): DBInstance {
        val describeDbInstancesRequest = DescribeDBInstancesRequest()
        val describeDbInstancesResponse = rdsClient.describeDBInstances(describeDbInstancesRequest)
        val dbInstances = describeDbInstancesResponse.dbInstances
        val identifierMatches = { instance: DBInstance -> instance.dbInstanceIdentifier == instanceIdentifier }
        val dbInstance = dbInstances.filter(identifierMatches).exactlyOne("Database instance with id $instanceIdentifier")
        return dbInstance

    }

    private fun waitForDbInstance(instanceIdentifier: String, p: (DBInstance) -> Boolean) {
        val instance = findInstanceByName(instanceIdentifier)
        retryDuration(howOftenToCheck, howLongToWait) {
            p(instance)
        }
    }

    private fun <T> withDbInstance(instanceIdentifier: String, f: (DBInstance) -> T): T {
        val instance = findInstanceByName(instanceIdentifier)
        return f(instance)
    }

    private fun <T> List<T>.exactlyOne(nameOfThingLookingFor: String): T = when (size) {
        1 -> get(0)
        else -> throw RuntimeException("Exactly one $nameOfThingLookingFor expected, got $size")
    }

    private val howOftenToCheck = Duration.ofSeconds(5)
    private val howLongToWait = Duration.ofHours(1)
}
