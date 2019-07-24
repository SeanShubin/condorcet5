package com.seanshubin.condorcet.domain.db

interface EventDbQueries {
    fun eventsToSync(lastEventSynced: Int): List<EventDetail>
    fun lastSynced(): Int?
}
