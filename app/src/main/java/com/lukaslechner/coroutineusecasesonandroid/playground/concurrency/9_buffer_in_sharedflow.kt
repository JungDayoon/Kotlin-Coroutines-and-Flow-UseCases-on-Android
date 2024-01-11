package com.lukaslechner.coroutineusecasesonandroid.playground.concurrency

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

suspend fun main(): Unit = coroutineScope {
    val flow = MutableSharedFlow<Int>(extraBufferCapacity = 10)
    // replay: 0 strategy: SUSPEND

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