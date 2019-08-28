package com.seanshubin.condorcet.provision

import com.amazonaws.services.cloudformation.model.StackStatus

interface Deployer {
    fun initiateDeploy()
    fun initiateTeardown()
    fun currentStatus(): StackStatus?
    fun waitForStatus(targetStatus: StackStatus, allowNull: Boolean = false)
}
