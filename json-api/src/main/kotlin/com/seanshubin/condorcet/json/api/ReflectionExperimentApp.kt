package com.seanshubin.condorcet.json.api

import com.fasterxml.jackson.module.kotlin.readValue
import com.seanshubin.condorcet.domain.Api
import com.seanshubin.condorcet.domain.Credentials
import com.seanshubin.condorcet.json.JsonUtil
import com.seanshubin.condorcet.util.exactlyOne
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions

fun KClass<*>.findSingleFunctionNamed(name: String): KFunction<*> =
        declaredFunctions.filter { it.name == name }.exactlyOne("declared function named '$name'")

data class Foo(val credentials: Credentials,
               val electionName: String,
               val eligibleVoterNames: List<String>)

inline fun <reified T> Map<String, Any>.readValue(key: String): T {
    println("key = $key")
    val value = get(key)
    println("value = $value")
    val json = JsonUtil.compact.writeValueAsString(value)
    println("json = $json")
    val typed = JsonUtil.parser.readValue<T>(json)
    println("typed = $typed")
    return typed
}

fun main() {
    val api: Api = ApiSample()
    val apiClass: KClass<Api> = Api::class
    val endpoint = "setVoters"
    val body = """{"credentials":{"userName":"user","userPassword":"password"},"electionName":"election","eligibleVoterNames":["alice","bob","carol"]}"""
    val parsed: Map<String, Any> = JsonUtil.parser.readValue<Any>(body) as Map<String, Any>
    val credentials: Credentials = parsed.readValue("credentials")
    val electionName: String = parsed.readValue("electionName")
    val eligibleVoterNames: List<String> = parsed.readValue("eligibleVoterNames")
    val setVotersFunction: KFunction<*> = apiClass.findSingleFunctionNamed(endpoint)
    val result = setVotersFunction.call(api, credentials, electionName, eligibleVoterNames)
    println(result)
//    val foo = Foo(Credentials("user", "password"), "election", listOf("alice", "bob", "carol"))
//    println(JsonUtil.compact.writeValueAsString(foo))
//    apiClass.declaredFunctions.forEach { declaredFunction: KFunction<*> ->
//        println(declaredFunction.name)
//        println("  given")
//        declaredFunction.parameters.forEach { parameter ->
//            println("    ${parameter.name}:${parameter.type}")
//        }
//        println("  return")
//        println("    ${declaredFunction.returnType}")
//    }

}