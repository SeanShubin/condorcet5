package com.seanshubin.condorcet.domain

import com.seanshubin.condorcet.crypto.*
import com.seanshubin.condorcet.domain.db.InMemoryDb
import java.time.Instant
import java.util.*

/*
reminder to test
- default
- seed1
- null
- whitespace in name
    - create
    - duplicate
- capitalization
    - duplicate
- authentication
- authorization
- missing
- duplicate
 */

object Tester {
    val validCredentials = Credentials("Alice", "password")
    val invalidCredentials = Credentials("Alice", "invalid-password")
    val nonOwnerCredentials = Credentials("Bob", "password")
    val allEligibleVoterNames = arrayOf("Alice", "Bob", "Carol", "Dave")
    val electionName = "New Election"
    val whitespaceBlock = Regex("""\s+""")
    val whitespaceNoiseBlock = " \r \n \t "
    val now = Instant.parse("2019-06-10T15:53:01.806Z")
    val clock = StoppedClock(now)
    val oneWayHash: OneWayHash = Sha256Hash()
    val uniqueIdGenerator: UniqueIdGenerator = Uuid4()
    val passwordUtil = PasswordUtil(uniqueIdGenerator, oneWayHash)
    val seed = 12345L
    val random = Random(seed)
    fun createEmpty(): Api {
        val db = InMemoryDb()
        val api = ApiBackedByDb(db, clock, passwordUtil, uniqueIdGenerator, random)
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

    fun createWithLiveElection(): Api {
        val api = createWithElection()
        api.doneEditingElection(validCredentials, electionName)
        return api
    }

    fun createWithElectionAndSeveralUsers(): Api {
        val api = createEmpty()
        api.register("Alice", "alice@email.com", "password")
        api.register("Bob", "bob@email.com", "password")
        api.register("Carol", "carol@email.com", "password")
        api.register("Dave", "dave@email.com", "password")
        api.createElection(validCredentials, electionName)
        return api
    }

    fun createWithElectionAndEligibleVoters(vararg voterNames: String): Api {
        val api = createWithElectionAndSeveralUsers()
        api.setVoters(validCredentials, electionName, voterNames.toList())
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
