package com.lukaslechner.coroutineusecasesonandroid.playground.concurrency

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

suspend fun main(): Unit = coroutineScope {
    val flow = MutableStateFlow(0)
    // replay: 1 strategy: DROP_OLDEST

    // Collector1
    launch {
        flow.collect {
            println("collector 1 processes $it")
        }
    }

    // Collector2
    launch {
        flow.collect {
            println("collector 2 processes $it")
            delay(100)
        }
    }


    // Emitter
    launch {
        val timeToEmit = measureTimeMillis {
            repeat(5) {
                flow.emit(it)
                delay(10)
            }
        }
        println("Time to emit all values: $timeToEmit ms")
    }
}