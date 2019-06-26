package com.seanshubin.condorcet.util.rds

import com.amazonaws.services.rds.AmazonRDSAsync
import com.amazonaws.services.rds.model.CreateDBInstanceRequest
import com.amazonaws.services.rds.model.DBInstance
import com.amazonaws.services.rds.model.DeleteDBInstanceRequest
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest
import com.seanshubin.condorcet.util.db.ResultSetUtil.columnNames
import com.seanshubin.condorcet.util.retryDuration
import java.sql.DriverManager
import java.time.Duration

class RdsDatabaseApiImpl(private val rdsClient: AmazonRDSAsync,
                         private val user: String,
                         private val password: String,
                         private val dbName: String) : RdsDatabaseApi {
    override fun createInstance(instanceIdentifier: String) {
        val createDbInstanceRequest = CreateDBInstanceRequest()
                .withDBInstanceIdentifier(instanceIdentifier)
                .withEngine("mysql")
                .withDBInstanceClass("db.t2.micro")
                .withMasterUsername(user)
                .withMasterUserPassword(password)
                .withDBName(dbName)
                .withAllocatedStorage(20)
                .withVpcSecurityGroupIds("vpc-772e0710")
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

    override fun createDatabase(instanceIdentifier: String, user: String, password: String, dbName: String) {
        val instance = findInstanceByIdentifier(instanceIdentifier)
        val endpoint = instance.endpoint.address
        val url = "jdbc:mysql://$endpoint"
        val connection = DriverManager.getConnection(url, user, password)
        val statement = connection.prepareCall("create database $dbName")
        statement.execute()
    }

    override fun listDatabases(instanceIdentifier: String): List<String> {
        val instance = findInstanceByIdentifier(instanceIdentifier)
        val endpoint = instance.endpoint.address
        val url = "jdbc:mysql://$endpoint"
        val connection = DriverManager.getConnection(url, user, password)
        val statement = connection.prepareStatement("show databases")
        val resultSet = statement.executeQuery()
        println(resultSet.columnNames())
        TODO()
    }

    private fun findInstanceByIdentifier(instanceIdentifier: String): DBInstance {
        val describeDbInstancesRequest = DescribeDBInstancesRequest()
        val describeDbInstancesResponse = rdsClient.describeDBInstances(describeDbInstancesRequest)
        val dbInstances = describeDbInstancesResponse.dbInstances
        val identifierMatches = { instance: DBInstance -> instance.dbInstanceIdentifier == instanceIdentifier }
        val dbInstance = dbInstances.filter(identifierMatches).exactlyOne("Database instance with id $instanceIdentifier")
        return dbInstance

    }

    private fun waitForDbInstance(instanceIdentifier: String, p: (DBInstance) -> Boolean) {
        val instance = findInstanceByIdentifier(instanceIdentifier)
        retryDuration(howOftenToCheck, howLongToWait) {
            p(instance)
        }
    }

    private fun <T> withDbInstance(instanceIdentifier: String, f: (DBInstance) -> T): T {
        val instance = findInstanceByIdentifier(instanceIdentifier)
        return f(instance)
    }

    private fun <T> List<T>.exactlyOne(nameOfThingLookingFor: String): T = when (size) {
        1 -> get(0)
        else -> throw RuntimeException("Exactly one $nameOfThingLookingFor expected, got $size")
    }

    private val howOftenToCheck = Duration.ofSeconds(5)
    private val howLongToWait = Duration.ofHours(1)
}
