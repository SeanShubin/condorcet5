package com.seanshubin.condorcet.provision

import com.seanshubin.condorcet.domain.db.Initializer

class ProvisionSetup(private val initializer: Initializer) :Runnable{
    override fun run() {
        initializer.initialize()
    }
}
