package com.seanshubin.condorcet.event

interface EventHandler {
    fun handle(event: Event)
}