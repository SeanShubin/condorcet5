package com.seanshubin.condorcet.integration

import com.seanshubin.condorcet.util.db.ConnectionLifecycle
import com.seanshubin.condorcet.util.db.JdbcConnectionLifecycle

object LocalConnectionLifecycle : ConnectionLifecycle by JdbcConnectionLifecycle(
        host = "localhost",
        user = "root",
        password = "insecure",
        database = "prototype")
