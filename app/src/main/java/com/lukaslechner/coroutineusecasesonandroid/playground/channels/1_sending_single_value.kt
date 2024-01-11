package com.lukaslechner.coroutineusecasesonandroid.playground.channels

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val deferred = async {
        delay(100)
        10
    }

    launch {
        val result = deferred.await()
        println(result)
    }
}