package com.seanshubin.condorcet.server

import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.json.api.JsonApi
import org.eclipse.jetty.server.Handler

class ApplicationDependencies(args: Array<String>) {
    val api: Api = TODO()
    val jsonApi: (String, String) -> String = JsonApi(api)
    val handler: Handler = CondorcetHandler(jsonApi)
    val runner = ApplicationRunner(4000, handler, JettyServerFactory::createWithPort)
}
