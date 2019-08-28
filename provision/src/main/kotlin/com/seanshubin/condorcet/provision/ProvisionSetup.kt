package com.seanshubin.condorcet.provision

import com.seanshubin.condorcet.domain.db.Initializer

class ProvisionSetup(private val deployer: Deployer,
                     private val initializer: Initializer) : Runnable {
    override fun run() {
        deployer.initiateDeploy()
        deployer.waitForDeployStatus(DeployStatus.DEPLOYED)
        initializer.initialize()
    }
}
