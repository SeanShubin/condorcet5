package com.seanshubin.condorcet.json.api

import com.seanshubin.condorcet.domain.Api

fun main() {
    val api: Api = ApiSample()
    val jsonApiBackedByApi = JsonApiBackedByApi(api)
    val endpoint = "setVoters"
    val requestBody = """{"credentials":{"userName":"user","userPassword":"password"},"electionName":"election","eligibleVoterNames":["alice","bob","carol"]}"""
    val responseBody = jsonApiBackedByApi.exec(endpoint, requestBody)
    println(responseBody)
}
