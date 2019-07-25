package com.seanshubin.condorcet.server

import org.eclipse.jetty.server.Handler

class Runner(private val configuration: Configuration,
             private val handler: Handler,
             private val createWithPort: (Int) -> JettyServerContract) : Runnable {
    override fun run() {
        val server = createWithPort(configuration.port)
        server.setHandler(handler)
        server.start()
        server.join()
    }
}
