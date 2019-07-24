package com.seanshubin.condorcet.domain.db

class ResourceEventDbCommands(private val dbFromResource: DbFromResource) :
        EventDbCommands,
        DbFromResource by dbFromResource {
    override fun addEvent(event: Event) {
        throw UnsupportedOperationException("Unable to insert events from here")
    }

    override fun setLastSynced(lastSynced: Int) {
        update("set-last-synced.sql", lastSynced)
    }

    override fun initializeLastSynced(lastSynced: Int) {
        update("initialize-last-synced.sql", lastSynced)
    }
}
