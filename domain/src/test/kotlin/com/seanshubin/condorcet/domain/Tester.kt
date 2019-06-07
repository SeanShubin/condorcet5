package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.memory.api.InMemoryDb

object Tester {
    val validCredentials = Credentials("Alice", "password")
    val invalidCredentials = Credentials("Alice", "invalid-password")
    val electionName = "New Election"
    val whitespaceBlock = Regex("""\s+""")
    val whitespaceNoiseBlock = " \r \n \t "
    fun createEmpty(): Api {
        val db = InMemoryDb()
        val api = ApiBackedByDb(db)
        return api
    }

    fun createWithUser(): Api {
        val api = createEmpty()
        api.register("ALice", "alice@email.com", "password")
        return api
    }

    fun createWithElection(): Api {
        val api = createWithUser()
        api.createElection(validCredentials, electionName)
        return api
    }

    fun String.addWhitespaceNoise(): String = replace(whitespaceBlock, whitespaceNoiseBlock)
}
