package com.seanshubin.condorcet.provision

import com.amazonaws.services.cloudformation.AmazonCloudFormation
import com.amazonaws.services.cloudformation.model.*

class SimplifiedCloudFormationAws(private val cloudFormation: AmazonCloudFormation) : SimplifiedCloudFormation {
    override fun stackExists(name: String): Boolean {
        val stacks = findStacksNamed(name)
        return when (stacks.size) {
            0 -> false
            1 -> true
            else -> throw RuntimeException("More than one stack named '$name' found")
        }
    }

    override fun stackStatus(name: String): StackStatus? {
        val stacks = findStacksNamed(name)
        return when (stacks.size) {
            0 -> null
            1 -> StackStatus.fromValue(stacks[0].stackStatus)
            else -> throw RuntimeException("More than one stack named '$name' found")
        }
    }

    override fun createStack(name: String, templateBody: String, parameters: Map<String, String>) {
        val awsParameters = parameters.map(AwsConversions::toParameter)
        val createStackRequest = CreateStackRequest()
                .withStackName(name)
                .withTemplateBody(templateBody)
                .withParameters(awsParameters)
        cloudFormation.createStack(createStackRequest)
    }

    override fun deleteStack(name: String) {
        val deleteStackRequest = DeleteStackRequest().withStackName(name)
        cloudFormation.deleteStack(deleteStackRequest)
    }

    private fun findStacksNamed(name: String): List<Stack> {
        val describeStacksRequest = DescribeStacksRequest()
        val describeStacksResponse = cloudFormation.describeStacks(describeStacksRequest)
        val stacks = describeStacksResponse.stacks.filter { it.stackName == name }
        return stacks
    }
}
