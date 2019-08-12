package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.json.JsonMappers
import javax.servlet.http.HttpServletResponse

class CondorcetHandler(private val jsonApi: (String, String) -> String) : ValueHandler {
    override fun handle(request: Request): Response =
            try {
                val command = commandFromUri(request.uri)
                val responseBody = jsonApi.invoke(command, request.body)
                Response(HttpServletResponse.SC_OK, responseBody).json()
            } catch (ex: Exception) {
                val responseBody = JsonMappers.pretty.writeValueAsString(ex)
                Response(HttpServletResponse.SC_BAD_REQUEST, responseBody).json()
            }

    private fun commandFromUri(path: String): String =
            if (path.startsWith("/")) {
                path.substring(1)
            } else {
                path
            }
}
