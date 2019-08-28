package com.seanshubin.condorcet.retry

import java.time.Duration

interface Retry {
    fun waitUntil(howOftenToCheck: Duration,
                  howLongToWait: Duration,
                  predicate: (Int) -> Boolean): Long
}