package com.seanshubin.condorcet.domain.db

interface EventDbCommands {
    fun addEvent(event: Event)
}
