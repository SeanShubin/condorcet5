package com.seanshubin.condorcet.domain

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class StoppedClock(val now: Instant) : Clock() {
    override fun withZone(zone: ZoneId?): Clock {
        TODO("not implemented")
    }

    override fun getZone(): ZoneId {
        TODO("not implemented")
    }

    override fun instant(): Instant = now
}