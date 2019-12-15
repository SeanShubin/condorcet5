package com.seanshubin.condorcet.logger

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class ClockStub(val zoneId: ZoneId,
                vararg val epochMilli: Long) : Clock() {
    var index = 0
    override fun withZone(zone: ZoneId?): Clock {
        throw UnsupportedOperationException()
    }

    override fun getZone(): ZoneId = zoneId

    override fun instant(): Instant {
        return Instant.ofEpochMilli(epochMilli[index++])
    }
}
