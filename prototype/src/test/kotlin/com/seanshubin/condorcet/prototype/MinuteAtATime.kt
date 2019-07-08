package com.seanshubin.condorcet.prototype

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class MinuteAtATime : Clock() {
    val baseline = Instant.parse("2019-07-08T22:00:08Z")
    var index = 0L
    override fun withZone(zone: ZoneId?): Clock {
        TODO("not implemented")
    }

    override fun getZone(): ZoneId {
        TODO("not implemented")
    }

    override fun instant(): Instant {
        val result = baseline.plus(index, ChronoUnit.MINUTES)
        index++
        return result
    }
}