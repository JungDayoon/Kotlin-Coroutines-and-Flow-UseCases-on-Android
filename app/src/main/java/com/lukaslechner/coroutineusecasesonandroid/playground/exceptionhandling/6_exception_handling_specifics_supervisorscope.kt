package com.lukaslechner.coroutineusecasesonandroid.playground.exceptionhandling

import kotlinx.coroutines.*

fun main() {
    val supervisorJob = SupervisorJob()
    val job = Job()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        println("Caught $throwable in CoroutineExceptionHandler, coroutineContext: $coroutineContext")
    }
    val scope = CoroutineScope(job + exceptionHandler)
    scope.launch {
        try {
//            supervisorScope {
                launch() {
                    println("CEH: ${coroutineContext[CoroutineExceptionHandler]}")
                    throw RuntimeException()
                }
//            }
        } catch (e: Exception) {
            println("Caught $e")
        }
    }

    scope.launch {
        delay(10)
        println("success")
    }

    Thread.sleep(100)
}