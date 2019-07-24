package com.seanshubin.condorcet.domain.db

interface EventHandler {
    fun handle(eventDetail: EventDetail)
}