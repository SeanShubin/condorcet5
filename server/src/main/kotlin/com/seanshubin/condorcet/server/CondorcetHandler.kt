package com.seanshubin.condorcet.server

import org.eclipse.jetty.server.Request
import org.eclipse.jetty.server.handler.AbstractHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CondorcetHandler(private val jsonApi: (String, String) -> String) : AbstractHandler() {
    override fun handle(target: String, baseRequest: Request, request: HttpServletRequest, response: HttpServletResponse) {
        println(request.remoteAddr)
        println(baseRequest.remoteAddr)
        jsonApi.invoke("", "")
        baseRequest.isHandled = true
    }
}