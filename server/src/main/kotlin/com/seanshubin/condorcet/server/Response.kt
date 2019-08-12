package com.seanshubin.condorcet.server

data class Response(val status: Int, val body: String, val headers: List<Pair<String, String>> = emptyList()) {
    fun json(): Response {
        val withoutContentType = headers.filter { (first, _) -> !first.equals("Content-Type", ignoreCase = true) }
        val withNewContentType = withoutContentType + Pair("Content-Type", "application/json")
        return copy(headers = withNewContentType)
    }
}
