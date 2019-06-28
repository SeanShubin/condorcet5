package com.seanshubin.condorcet.integration

import com.seanshubin.condorcet.domain.Api

interface ApiLifecycle {
    fun <T> withApi(f: (Api) -> T): T
}
