package com.lukaslechner.coroutineusecasesonandroid.playground.flow.intermediate_operators

import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

suspend fun main() {
    flowOf(1, 1, 2, 3, 3, 3, 4, 5, 1)
        .distinctUntilChanged()
        .collect { collectedValue ->
            println(collectedValue)
        }
}