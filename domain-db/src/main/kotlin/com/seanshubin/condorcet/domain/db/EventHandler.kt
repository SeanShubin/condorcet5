package com.seanshubin.condorcet.domain.db

interface EventHandler {
    fun handle(initiator: Initiator, event: Event)
}