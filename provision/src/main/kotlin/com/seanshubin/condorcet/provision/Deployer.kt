package com.seanshubin.condorcet.provision

interface Deployer {
    fun initiateDeploy()
    fun initiateTeardown()
    fun deployStatus(): DeployStatus
    fun waitForDeployStatus(deployStatus: DeployStatus)
}
