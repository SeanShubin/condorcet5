package com.seanshubin.condorcet.domain.db

class EventSynchronizer(private val eventDb: EventDb,
                        private val query: MutableDbQueries,
                        private val command: MutableDbCommands,
                        private val eventHandler: (Event) -> Unit) : Synchronizer {
    override fun synchronize() {
        val lastEventSynced = query.lastEventSynced()
        val eventsToSync = eventDb.eventsToSync(lastEventSynced)
        eventsToSync.forEach(eventHandler)
    }
}
