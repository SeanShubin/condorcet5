package com.seanshubin.condorcet.logger

class EmitStub : (String) -> Unit {
    var invocations = mutableListOf<String>()
    override fun invoke(s: String) {
        invocations.add(s)
    }
}
