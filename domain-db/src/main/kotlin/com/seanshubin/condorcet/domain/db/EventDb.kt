package com.seanshubin.condorcet.domain.db

interface EventDb {
    fun eventsToSync(lastEventSynced: Int): List<Event>
}
