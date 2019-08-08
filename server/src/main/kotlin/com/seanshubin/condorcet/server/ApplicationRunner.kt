package com.seanshubin.condorcet.server

import org.eclipse.jetty.server.Handler

class ApplicationRunner(private val handler: Handler,
                        private val createJetty: () -> JettyServerContract) : Runnable {
    override fun run() {
        val server = createJetty()
        server.setHandler(handler)
        server.start()
        server.join()
    }
}
