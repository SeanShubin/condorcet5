package com.seanshubin.condorcet.server

import org.eclipse.jetty.server.Server

object JettyServerFactory {
    fun createWithPort(port: Int): JettyServerContract = JettyServerDelegate(Server(port))
}
