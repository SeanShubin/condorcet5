package com.seanshubin.condorcet.prototype

import com.amazonaws.services.cloudformation.model.Parameter

object AwsConversions {
    fun toParameter(pair: Pair<String, String>): Parameter =
            Parameter().withParameterKey(pair.first).withParameterValue(pair.second)
}
