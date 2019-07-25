package com.seanshubin.condorcet.server

import org.eclipse.jetty.server.Handler

interface JettyServerContract {
    fun setHandler(handler: Handler)
    fun start()
    fun join()
}
