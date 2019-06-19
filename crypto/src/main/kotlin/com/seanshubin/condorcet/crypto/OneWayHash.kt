package com.seanshubin.condorcet.crypto

interface OneWayHash {
    fun hash(s: String): String
}
