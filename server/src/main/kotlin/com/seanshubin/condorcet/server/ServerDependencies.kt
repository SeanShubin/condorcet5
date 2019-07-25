package com.seanshubin.condorcet.server

import org.eclipse.jetty.server.Handler
import java.nio.file.Paths

class ServerDependencies(args: Array<String>) {
    val configuration: Configuration = Configuration(port = 4000, logFileBaseDir = Paths.get("out", "logs"))
    val handler: Handler = CondorcetHandler()
    val runner = Runner(configuration, handler, JettyServerFactory::createWithPort)
}
