package com.seanshubin.condorcet.crypto

import java.util.*

class Uuid4 : UniqueIdGenerator {
    override fun uniqueId(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }
}
