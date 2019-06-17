package com.seanshubin.condorcet.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.Duration

fun retryDuration(howOftenToCheck: Duration,
                  howLongToWait: Duration,
                  predicate: (Int) -> Boolean) {
    val started = System.currentTimeMillis()
    var tries = 0
    runBlocking {
        while (true) {
            tries++
            val result = predicate(tries)
            if (result) {
                break
            } else {
                val timePassed = Duration.ofMillis(System.currentTimeMillis() - started)
                if (timePassed > howLongToWait) {
                    throw RuntimeException(
                            "Timed out after $tries trys, " +
                                    "check every $howOftenToCheck, " +
                                    "give up after $howLongToWait")
                } else {
                    delay(howOftenToCheck.toMillis())
                }
            }
        }
    }
}
