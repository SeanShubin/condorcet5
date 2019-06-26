package com.seanshubin.condorcet.prototype

import com.amazonaws.services.cloudformation.AmazonCloudFormationClientBuilder
import com.amazonaws.services.cloudformation.model.CreateStackRequest
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest
import com.seanshubin.condorcet.util.ClassLoaderUtil
import com.seanshubin.condorcet.util.exactlyOne
import com.seanshubin.condorcet.util.retryDuration
import java.time.Duration

fun main() {
    val templateResourcePath = "lamp.json"
    val templateBody = ClassLoaderUtil.loadResourceAsString(templateResourcePath)
    val builder = AmazonCloudFormationClientBuilder.standard()
    val cloudFormation = builder.withRegion(awsRegion).build()
    val parameters = listOf(
            Pair("KeyName", "lamp-sample"),
            Pair("DBName", "lampdb"),
            Pair("DBUser", "lampuser"),
            Pair("DBPassword", "lamppassword"),
            Pair("DBRootPassword", "lamprootpassword"),
            Pair("InstanceType", "t2.micro"),
            Pair("SSHLocation", "123.123.123.123/24")
    ).map(AwsConversions::toParameter)
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
/*
{
    "SSHLocation" : {
      "Description" : " The IP address range that can be used to SSH to the EC2 instances",
      "Type": "String",
      "MinLength": "9",
      "MaxLength": "18",
      "Default": "0.0.0.0/0",
      "AllowedPattern": "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,2})",
      "ConstraintDescription": "must be a valid IP CIDR range of the form x.x.x.x/x."
    }
  },
 */