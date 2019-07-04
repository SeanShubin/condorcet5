package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.util.db.ConnectionInfo

object Connections {
    val local = ConnectionInfo(
            host = "localhost",
            user = "root",
            password = "insecure")
}
