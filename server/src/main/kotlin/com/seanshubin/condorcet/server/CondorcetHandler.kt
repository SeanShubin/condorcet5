package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.json.JsonMappers
import com.seanshubin.condorcet.util.IoUtil.consumeToString
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CondorcetHandler(private val jsonApi: (String, String) -> String) : AbstractHandler() {
    override fun handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
        try {
            val command = commandFromPath(request.pathInfo)
            val requestBody = request.reader.consumeToString()
            val responseBody = jsonApi.invoke(command, requestBody)
            response.writer.write(responseBody)
            response.status = HttpServletResponse.SC_OK
        } catch (ex: Exception) {
            JsonMappers.pretty.writeValue(response.writer, ex)
            response.status = HttpServletResponse.SC_BAD_REQUEST
        }
        response.contentType = "application/json"
        baseRequest.isHandled = true
    }

    private fun commandFromPath(path: String): String =
            if (path.startsWith("/")) {
                path.substring(1)
            } else {
                path
            }
}
