package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.memory.api.InMemoryDb

object Tester {
    val validCredentials = Credentials("Alice", "password")
    val invalidCredentials = Credentials("Alice", "invalid-password")
    val nonOwnerCredentials = Credentials("Bob", "password")
    val electionName = "New Election"
    val whitespaceBlock = Regex("""\s+""")
    val whitespaceNoiseBlock = " \r \n \t "
    fun createEmpty(): Api {
        val db = InMemoryDb()
        val api = ApiBackedByDb(db)
        return api
    }

    fun createWithUsers(): Api {
        val api = createEmpty()
        api.register("Alice", "alice@email.com", "password")
        api.register("Bob", "bob@email.com", "password")
        return api
    }

    fun createWithElection(): Api {
        val api = createWithUsers()
        api.createElection(validCredentials, electionName)
        return api
    }

    fun String.addWhitespaceNoise(): String = replace(whitespaceBlock, whitespaceNoiseBlock)
    fun String.invertCapitalization(): String = this.map {
        when {
            it.isUpperCase() -> it.toLowerCase()
            it.isLowerCase() -> it.toUpperCase()
            else -> it
        }
    }.joinToString("")
}
