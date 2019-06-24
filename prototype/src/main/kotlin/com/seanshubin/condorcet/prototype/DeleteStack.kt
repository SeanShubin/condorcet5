package com.seanshubin.condorcet.prototype

import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder
import com.amazonaws.services.cloudformation.model.DeleteStackRequest

fun main() {
    val builder = AmazonCloudFormationClientBuilder.standard()
    val cloudFormation = builder.withRegion(awsRegion).build()
    val deleteStackRequest = DeleteStackRequest()
            .withStackName(stackName)
    val response = cloudFormation.deleteStack(deleteStackRequest)
    println(response)

}