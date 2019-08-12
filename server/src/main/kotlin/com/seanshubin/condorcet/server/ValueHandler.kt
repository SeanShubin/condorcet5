package com.seanshubin.condorcet.server

interface ValueHandler {
    fun handle(request: Request): Response
}
