package com.seanshubin.condorcet.script

import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest
import com.seanshubin.condorcet.util.exactlyOne

fun main() {
    val builder = AmazonCloudFormationClientBuilder.standard()
    val cloudFormation = builder.withRegion(awsRegion).build()
    val describeStacksRequest = DescribeStacksRequest().withStackName(stackName)
    val describeStacksResponse = cloudFormation.describeStacks(describeStacksRequest)
    val stack = describeStacksResponse.stacks.exactlyOne("Stack named '$stackName'")
    println(stack)

}