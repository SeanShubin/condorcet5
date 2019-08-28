package com.seanshubin.condorcet.provision

import com.amazonaws.services.cloudformation.model.StackStatus
import com.seanshubin.condorcet.retry.Retry
import com.seanshubin.condorcet.util.DurationFormat
import java.time.Duration

class CloudFormationDeployer(private val cloudFormation: SimplifiedCloudFormation,
                             private val stackName: String,
                             private val templateResource: String,
                             private val loadResource: (String) -> String,
                             private val retry: Retry,
                             private val stackStatusUpdate: (String) -> Unit) : Deployer {
    override fun initiateDeploy() {
        if (cloudFormation.stackExists(stackName)) {
            throw RuntimeException("Stack named '$stackName' already exists")
        }
        val parameters = mapOf(
                "KeyName" to "lamp-sample",
                "DBName" to "lampdb",
                "DBUser" to "lampuser",
                "DBPassword" to "lamppassword",
                "DBRootPassword" to "lamprootpassword",
                "InstanceType" to "t2.micro",
                "SSHLocation" to "0.0.0.0/0")
        val templateBody = loadResource(templateResource)
        cloudFormation.createStack(stackName, templateBody, parameters)
    }

    override fun initiateTeardown() {
        cloudFormation.deleteStack(stackName)
    }

    override fun currentStatus(): StackStatus? = cloudFormation.stackStatus(stackName)

    override fun waitForStatus(targetStatus: StackStatus, allowNull: Boolean) {
        stackStatusUpdate("Waiting for status $targetStatus on stack named '$stackName'")
        var currentStatus: StackStatus? = null
        val millisecondsTaken = retry.waitUntil(Duration.ofSeconds(5), Duration.ofMinutes(30)) { tryIndex ->
            currentStatus = currentStatus()
            stackStatusUpdate("($tryIndex) Status for stack named '$stackName' is $currentStatus")
            if (currentStatus == null) {
                allowNull
            } else {
                currentStatus == targetStatus
            }
        }
        val millisecondsString = DurationFormat.milliseconds.format(millisecondsTaken)
        stackStatusUpdate("Stack named '$stackName' changed to status $currentStatus in $millisecondsString")
    }
}
