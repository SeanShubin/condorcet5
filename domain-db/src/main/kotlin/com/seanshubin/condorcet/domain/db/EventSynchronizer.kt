package com.seanshubin.condorcet.domain.db

class EventSynchronizer(private val eventDbQueries: EventDbQueries,
                        private val mutableDbQueries: MutableDbQueries,
                        private val eventHandler: (InitiatorAndEvent) -> Unit) : Synchronizer {
    override fun synchronize() {
        val lastEventSynced = mutableDbQueries.lastEventSynced()
        val eventsToSync = eventDbQueries.eventsToSync(lastEventSynced)
        eventsToSync.forEach(eventHandler)
    }
}
