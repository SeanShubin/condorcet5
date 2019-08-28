package com.seanshubin.condorcet.provision

class ProvisionTeardown(private val deployer: Deployer) : Runnable {
    override fun run() {
        deployer.initiateTeardown()
        deployer.waitForDeployStatus(DeployStatus.MISSING)
    }
}
