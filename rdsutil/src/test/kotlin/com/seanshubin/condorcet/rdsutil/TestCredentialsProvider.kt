package com.seanshubin.condorcet.rdsutil

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider

object TestCredentialsProvider : AWSCredentialsProvider {
    override fun getCredentials(): AWSCredentials =
            object : AWSCredentials {
                override fun getAWSAccessKeyId(): String =
                        "***"

                override fun getAWSSecretKey(): String =
                        "***"
            }

    override fun refresh() {
    }
}