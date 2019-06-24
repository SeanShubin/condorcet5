package com.seanshubin.condorcet.domain.db.event

interface EventHandler {
    fun handle(event: Event)
}