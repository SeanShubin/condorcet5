package com.seanshubin.condorcet.domain.db

interface EventDbCommands {
    fun addEvent(event: Event)
    fun setLastSynced(lastSynced: Int)
    fun initializeLastSynced(lastSynced: Int)
}
