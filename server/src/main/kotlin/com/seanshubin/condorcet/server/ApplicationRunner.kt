package com.seanshubin.condorcet.server

import org.eclipse.jetty.server.Handler

class ApplicationRunner(private val port: Int,
                        private val handler: Handler,
                        private val createWithPort: (Int) -> JettyServerContract) : Runnable {
    override fun run() {
        val server = createWithPort(port)
        server.setHandler(handler)
        server.start()
        server.join()
    }
}
