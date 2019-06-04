package com.seanshubin.condorcet.rdsutil

import com.amazonaws.services.rds.AmazonRDSAsync
import com.amazonaws.services.rds.model.CreateDBInstanceRequest
import com.amazonaws.services.rds.model.DeleteDBInstanceRequest
import com.amazonaws.services.rds.model.DescribeDBInstancesRequest
import com.seanshubin.condorcet.util.retryDuration
import java.time.Duration

class RdsDatabaseApiImpl(private val rdsClient: AmazonRDSAsync) : RdsDatabaseApi {
    override fun createDatabase(instanceIdentifier: String, masterUserPassword: String) {
        val createDbInstanceRequest = CreateDBInstanceRequest().withMasterUserPassword(masterUserPassword).withDBInstanceIdentifier(instanceIdentifier).withEngine("mysql").withDBInstanceClass("db.t2.micro").withMasterUsername("sean").withAllocatedStorage(20)
        rdsClient.createDBInstance(createDbInstanceRequest)
    }

    override fun databaseExists(instanceIdentifier: String): Boolean {
        val describeDbInstancesRequest = DescribeDBInstancesRequest()
        val describeDbInstancesResponse = rdsClient.describeDBInstances(describeDbInstancesRequest)
        val dbInstances = describeDbInstancesResponse.dbInstances
        return dbInstances.any { dbInstance -> dbInstance.dbInstanceIdentifier == instanceIdentifier }
    }

    override fun deleteDatabase(instanceIdentifier: String) {
        val deleteDbInstanceRequest = DeleteDBInstanceRequest().withDBInstanceIdentifier(instanceIdentifier).withSkipFinalSnapshot(true)
        rdsClient.deleteDBInstanceAsync(deleteDbInstanceRequest).get()
    }

    override fun waitForDatabaseToGoAway(instanceIdentifier: String) {
        val howOftenToCheck = Duration.ofSeconds(5)
        val howLongToWait = Duration.ofMinutes(2)
        retryDuration(howOftenToCheck, howLongToWait) {
            databaseExists(instanceIdentifier)
        }
    }
}
