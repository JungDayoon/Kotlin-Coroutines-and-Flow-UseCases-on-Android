package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler")
    }
    val scope = CoroutineScope(Job())
    scope.launch(exceptionHandler) {
        launch {
            println("Starting Coroutine 1")
            delay(100)
            throw RuntimeException()
        }

        launch {
            println("Starting Coroutine 2")
            delay(3000)
            println("Coroutine 2 completed")
        }
    }

    Thread.sleep(5000)
}