package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.util.db.ConnectionLifecycle
import com.seanshubin.condorcet.util.db.JdbcConnectionLifecycle

object LocalConnectionLifecycle : ConnectionLifecycle by JdbcConnectionLifecycle(
        host = "localhost",
        user = "root",
        password = "insecure",
        database = "prototype")
