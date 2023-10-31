package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

suspend fun main(): Unit = coroutineScope {
    val stocksFlow = stocksFlow()

    stocksFlow
        .onCompletion { cause ->
            if (cause == null) {
                println("Flow completed successfully!")
            } else {
                println("Flow completed exceptionally with $cause")
            }
        }
        .onEach { stock ->
            throw Exception("Exception in collect{}")
            println("Collected $stock")
        }
        .catch { throwable ->
            println("Handle exception in catch() operator $throwable")
        }
        .launchIn(this)
}

private fun stocksFlow(): Flow<String> = flow {
    emit("Apple")
    emit("Microsoft")
    throw Exception("Network request failed")
}
