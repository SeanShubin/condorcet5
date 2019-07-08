package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.crypto.UniqueIdGenerator

class DeterministicUniqueIdGenerator : UniqueIdGenerator {
    var index = 0
    override fun uniqueId(): String = "unique-id-${++index}"
}