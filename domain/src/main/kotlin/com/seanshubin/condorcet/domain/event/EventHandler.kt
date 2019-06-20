package com.seanshubin.condorcet.domain.event

interface EventHandler {
    fun handle(event: Event)
}