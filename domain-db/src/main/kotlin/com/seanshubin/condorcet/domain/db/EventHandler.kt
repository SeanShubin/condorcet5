package com.seanshubin.condorcet.domain.db

interface EventHandler {
    fun handle(event: Event)
}