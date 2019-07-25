package com.seanshubin.condorcet.server

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.server.Server

class JettyServerDelegate(private val jettyServer: Server) : JettyServerContract {
    override fun setHandler(handler: Handler) {
        jettyServer.handler = handler
    }

    override fun start() {
        jettyServer.start()
    }

    override fun join() {
        jettyServer.join()
    }
}
