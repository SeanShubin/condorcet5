package com.seanshubin.condorcet.algorithm

data class SecretBallot(val confirmation: String,
                        val rankings: Map<String, Int>)
