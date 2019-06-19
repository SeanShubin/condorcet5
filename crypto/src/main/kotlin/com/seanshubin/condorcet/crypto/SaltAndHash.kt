package com.seanshubin.condorcet.crypto

data class SaltAndHash(val salt: String, val hash: String) {
    companion object {
        fun fromPassword(
                password: String,
                uniqueIdGenerator: UniqueIdGenerator,
                oneWayHash: OneWayHash
        ): SaltAndHash {
            val salt = uniqueIdGenerator.uniqueId()
            val hash = oneWayHash.hash(salt + password)
            return SaltAndHash(salt, hash)
        }

        fun validate(
                password: String,
                saltAndHash: SaltAndHash,
                oneWayHash: OneWayHash
        ): Boolean {
            val (salt, expectedHash) = saltAndHash
            val actualHash = oneWayHash.hash(salt + password)
            return actualHash == expectedHash
        }
    }
}
