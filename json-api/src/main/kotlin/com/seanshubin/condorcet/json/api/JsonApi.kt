package com.seanshubin.condorcet.json.api

import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.json.JsonMappers

class JsonApi(private val api: Api) : (String, String) -> String {
    override fun invoke(name: String, body: String): String {
        val request = Request.parse(name, body)
        val response = request.invoke(api)
        return JsonMappers.pretty.writeValueAsString(response)
    }
}
