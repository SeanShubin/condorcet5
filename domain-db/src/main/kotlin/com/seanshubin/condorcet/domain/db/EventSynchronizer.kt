package com.seanshubin.condorcet.domain.db

class EventSynchronizer(private val eventDbQueries: EventDbQueries,
                        private val eventDbCommands: EventDbCommands,
                        private val eventHandler: EventHandler) : Synchronizer {
    override tailrec fun synchronize() {
        val lastEventSynced = eventDbQueries.lastSynced()
        if (lastEventSynced == null) {
            eventDbCommands.setLastSynced(0)
            synchronize()
        } else {
            val eventsToSync = eventDbQueries.eventsToSync(lastEventSynced)
            eventsToSync.forEach {
                eventHandler.handle(it)
                eventDbCommands.setLastSynced(it.id)
            }
        }
    }
}
