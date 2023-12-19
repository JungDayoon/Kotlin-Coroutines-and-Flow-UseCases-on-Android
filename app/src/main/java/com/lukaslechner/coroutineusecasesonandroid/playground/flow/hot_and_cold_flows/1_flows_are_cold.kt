package com.lukaslechner.coroutineusecasesonandroid.playground.flow.hot_and_cold_flows

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun coldFlow() = flow {
    println("Emitting 1")
    emit(1)

    delay(1000)
    println("Emitting 2")
    emit(2)

    delay(1000)
    println("Emitting 3")
    emit(3)
}

suspend fun main(): Unit = coroutineScope {
    // #1 become active on collection demonstrate
//    coldFlow()
//        .collect {
//            println("Collector 1 collects: $it")
//        }


    // #2 become inactive on cancellation of the collecting coroutine demonstrate
//    var job = launch {
//        coldFlow()
//            .onCompletion {
//                println("Flow of Collector 1 completed")
//            }
//            .collect {
//                println("Collector 1 collects: $it")
//            }
//    }
//    delay(1000)
//    job.cancelAndJoin()

    // #3 emit individual emissions to every collector demonstrate
    launch {
        coldFlow()
            .collect {
                println("Collector 1 collects: $it")
            }
    }

    launch {
        coldFlow()
            .collect {
                println("Collector 2 collects: $it")
            }
    }
}