package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.domain.db.Initializer
import org.eclipse.jetty.server.Handler

class ApplicationRunner(private val initializer: Initializer,
                        private val handler: Handler,
                        private val createJetty: () -> JettyServerContract) : Runnable {
    override fun run() {
        initializer.initialize()
        val server = createJetty()
        server.setHandler(handler)
        server.start()
        server.join()
    }
}
