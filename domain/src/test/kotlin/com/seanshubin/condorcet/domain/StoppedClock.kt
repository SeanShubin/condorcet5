package com.seanshubin.condorcet.domain

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class StoppedClock(val now: Instant) : Clock() {
    override fun withZone(zone: ZoneId?): Clock {
        throw UnsupportedOperationException()
    }

    override fun getZone(): ZoneId {
        throw UnsupportedOperationException()
    }

    override fun instant(): Instant = now
}