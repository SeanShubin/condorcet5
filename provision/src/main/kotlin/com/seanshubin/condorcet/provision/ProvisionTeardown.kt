package com.seanshubin.condorcet.provision

import com.amazonaws.services.cloudformation.model.StackStatus

class ProvisionTeardown(private val deployer: Deployer) : Runnable {
    override fun run() {
        deployer.initiateTeardown()
        deployer.waitForStatus(StackStatus.DELETE_COMPLETE, allowNull = true)
    }
}
