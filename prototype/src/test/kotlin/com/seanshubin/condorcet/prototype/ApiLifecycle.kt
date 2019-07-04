package com.seanshubin.condorcet.prototype

import com.seanshubin.condorcet.domain.Api

interface ApiLifecycle {
    fun <T> withApi(f: (Api) -> T): T
}
