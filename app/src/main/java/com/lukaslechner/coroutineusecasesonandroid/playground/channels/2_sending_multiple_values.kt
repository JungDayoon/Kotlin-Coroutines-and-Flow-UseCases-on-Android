package com.lukaslechner.coroutineusecasesonandroid.playground.channels

import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun main(): Unit = coroutineScope {
    val channel = produce<Int>{
        println("sending 10")
        send(10)

        println("sending 20")
        send(20)
    }

    launch {
        channel.consumeEach { receivedValue ->
            println("consumer1: $receivedValue")

        }
    }

    launch {
        channel.consumeEach { receivedValue ->
            println("consumer2: $receivedValue")

        }
    }
}