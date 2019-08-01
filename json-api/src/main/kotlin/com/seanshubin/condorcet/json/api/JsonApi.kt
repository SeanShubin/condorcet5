package com.seanshubin.condorcet.json.api

interface JsonApi {
    fun exec(name: String, body: String): String
}
