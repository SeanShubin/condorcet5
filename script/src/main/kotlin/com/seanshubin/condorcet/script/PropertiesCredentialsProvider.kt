package com.seanshubin.condorcet.script

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import java.util.*

class PropertiesCredentialsProvider(val properties: Properties) : AWSCredentialsProvider {
    override fun getCredentials(): AWSCredentials =
            object : AWSCredentials {
                override fun getAWSAccessKeyId(): String =
                        properties.getProperty("aws_access_key_id")

                override fun getAWSSecretKey(): String =
                        properties.getProperty("aws_secret_access_key")
            }

    override fun refresh() {
    }
}
