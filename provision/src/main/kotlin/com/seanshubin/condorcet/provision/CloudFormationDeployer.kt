package com.seanshubin.condorcet.provision

import com.amazonaws.services.cloudformation.AmazonCloudFormation

class CloudFormationDeployer(val cloudFormation: AmazonCloudFormation) : Deployer {
    override fun initiateDeploy() {
        val parameters = listOf(
                Pair("KeyName", "lamp-sample"),
                Pair("DBName", "lampdb"),
                Pair("DBUser", "lampuser"),
                Pair("DBPassword", "lamppassword"),
                Pair("DBRootPassword", "lamprootpassword"),
                Pair("InstanceType", "t2.micro"),
                Pair("SSHLocation", "0.0.0.0/0")
        ).map(AwsConversions::toParameter)
    }

    override fun initiateTeardown() {
        TODO("not implemented")
    }

    override fun deployStatus(): DeployStatus {
        TODO("not implemented")
    }

    override fun waitForDeployStatus(deployStatus: DeployStatus) {
        cloudFormation.listStacks().stackSummaries.forEach {
            println(it)
        }
    }
}
