package com.seanshubin.condorcet.provision

import com.amazonaws.services.cloudformation.model.StackStatus

interface SimplifiedCloudFormation {
    fun stackExists(name: String): Boolean
    fun stackStatus(name: String): StackStatus?
    fun createStack(name: String, templateBody: String, parameters: Map<String, String>)
    fun deleteStack(name: String)
}
