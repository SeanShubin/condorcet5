package com.seanshubin.condorcet.domain.db

import java.time.Instant

data class EventDetail(
        val id: Int,
        val source: String,
        val owner: String,
        val whenHappened: Instant,
        val event: Event) {
    val initiator: Initiator get() = Initiator(source, owner)
}
