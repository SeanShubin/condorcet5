package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.util.IoUtil.consumeToString
import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ValueHandlerToJettyHandlerAdapter(private val valueHandler: ValueHandler) : AbstractHandler() {
    override fun handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
        val uri = request.pathInfo
        val method = request.method
        val requestBody = request.reader.consumeToString()
        val requestValue = Request(method, uri, requestBody)
        val responseValue = valueHandler.handle(requestValue)
        response.writer.write(responseValue.body)
        response.status = responseValue.status
        baseRequest.isHandled = true
    }
}
