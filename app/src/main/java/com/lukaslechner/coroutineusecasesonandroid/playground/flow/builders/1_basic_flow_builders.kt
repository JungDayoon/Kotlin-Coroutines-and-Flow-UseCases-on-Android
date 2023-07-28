package com.lukaslechner.coroutineusecasesonandroid.playground.flow.builders

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

// fun main() = runBlocking {..} 과 동일함
suspend fun main() {
    val firstFlow = flowOf<Int>(1).collect { emittedValue ->
        println("firstFlow: $emittedValue")
    }

    val secondFlow = flowOf(1, 2, 3)
    secondFlow.collect {
        println("secondFlow: $it")
    }

    listOf("A", "B", "C").asFlow().collect { emittedValue ->
        println("asFlow: $emittedValue")

    }

    flow {
        delay(2000)
        emit("item emitted after 2000ms")

        secondFlow.collect {
            emit(it)
        }
    }.collect {
        println("flow{}: $it")
    }
}