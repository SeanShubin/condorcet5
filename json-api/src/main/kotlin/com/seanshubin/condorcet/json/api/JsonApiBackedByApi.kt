package com.seanshubin.condorcet.json.api

import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.json.JsonMappers

class JsonApiBackedByApi(private val api: Api) : JsonApi {
    override fun exec(name: String, body: String): String {
        val request = Request.parse(name, body)
        val response = request.exec(api)
        return JsonMappers.pretty.writeValueAsString(response)
    }
}
