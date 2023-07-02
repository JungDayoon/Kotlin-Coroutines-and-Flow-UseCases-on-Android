package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler")
    }

    val scope = CoroutineScope(Job() + exceptionHandler)
    scope.async {
        async {
            delay(200)
            throw RuntimeException()
        }
    }
    Thread.sleep(500)
}