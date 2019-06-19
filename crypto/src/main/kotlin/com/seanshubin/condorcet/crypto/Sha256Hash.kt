package com.seanshubin.condorcet.crypto

import java.security.MessageDigest

class Sha256Hash : OneWayHash {
    companion object {
        private val messageDigest: MessageDigest by lazy {
            MessageDigest.getInstance("SHA-256")
        }
    }

    override fun hash(s: String): String {
        val inputBytes = s.toByteArray()
        val hashedBytes = messageDigest.digest(inputBytes)
        val hashedString = hex(hashedBytes)
        return hashedString
    }

    private fun hex(byteArray: ByteArray): String = byteArray.joinToString("") { hex(it) }

    private fun hex(byte: Byte): String {
        val asInt = byte.toInt()
        val digits = "0123456789ABCDEF"
        val lowByte = asInt and 0b1111
        val highByte = asInt shr 4 and 0b1111
        val lowDigit = digits[lowByte]
        val highDigit = digits[highByte]
        return "" + highDigit + lowDigit
    }
}
