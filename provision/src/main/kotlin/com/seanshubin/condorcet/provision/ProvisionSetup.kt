package com.seanshubin.condorcet.provision

import com.amazonaws.services.cloudformation.model.StackStatus
import com.seanshubin.condorcet.domain.db.Initializer

class ProvisionSetup(private val deployer: Deployer,
                     private val initializer: Initializer) : Runnable {
    override fun run() {
        deployer.initiateDeploy()
        deployer.waitForStatus(StackStatus.CREATE_COMPLETE)
        initializer.initialize()
    }
}
