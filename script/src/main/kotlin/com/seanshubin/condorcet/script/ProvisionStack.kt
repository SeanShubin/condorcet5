package com.seanshubin.condorcet.script

import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder
import com.amazonaws.services.cloudformation.model.CreateStackRequest
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest
import com.seanshubin.condorcet.script.AwsConversions.toParameter
import com.seanshubin.condorcet.util.ClassLoaderUtil.loadResourceAsString
import com.seanshubin.condorcet.util.exactlyOne
import com.seanshubin.condorcet.util.retryDuration
import java.time.Duration

fun main() {
    val templateResourcePath = "persistence.yaml"
    val templateBody = loadResourceAsString(templateResourcePath)
    val builder = AmazonCloudFormationClientBuilder.standard()
    val cloudFormation = builder.withRegion(awsRegion).build()
    val parameters = listOf(
            Pair("DBName", dbName),
            Pair("DBUser", dbUser),
            Pair("DBPassword", dbPassword)).map(::toParameter)
    val createStackRequest = CreateStackRequest()
            .withStackName(stackName)
            .withTemplateBody(templateBody)
            .withParameters(parameters)
    val createStackResponse = cloudFormation.createStack(createStackRequest)
    fun statusCheck(tryIndex: Int): Boolean {
        val describeStacksRequest = DescribeStacksRequest().withStackName(stackName)
        val describeStacksResponse = cloudFormation.describeStacks(describeStacksRequest)
        val stack = describeStacksResponse.stacks.exactlyOne("Stack named '$stackName'")
        val status = stack.stackStatus
        println("[$tryIndex] status of $stackName is $status")
        return status == "CREATE_COMPLETE"

    }
    retryDuration(Duration.ofSeconds(5), Duration.ofMinutes(30), ::statusCheck)
    println(createStackResponse)

}