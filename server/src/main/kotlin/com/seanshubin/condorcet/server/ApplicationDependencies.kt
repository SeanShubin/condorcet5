package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.json.api.JsonApi
import org.eclipse.jetty.server.Handler
import java.nio.file.Paths

class ApplicationDependencies(args: Array<String>) {
    val configuration: Configuration = Configuration(port = 4000, logFileBaseDir = Paths.get("out", "logs"))
    val api: Api = TODO()
    val jsonApi: (String, String) -> String = JsonApi(api)
    val handler: Handler = CondorcetHandler(jsonApi)
    val runner = ApplicationRunner(configuration, handler, JettyServerFactory::createWithPort)
}
