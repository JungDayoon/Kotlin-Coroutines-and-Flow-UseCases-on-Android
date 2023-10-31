package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.math.BigInteger
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main() {
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        intFlow()
            .onCompletion { cause: Throwable? ->
                if (cause is CancellationException) {
                    println("Flow got cancelled")
                }
            }
            .collect {
                println("Collected $it")
                if (it == 2) {
                    cancel()
                }
            }
    }.join()
}

private fun intFlow() = flow {
    emit(1)
    emit(2)

//    currentCoroutineContext().ensureActive()

    println("Start calculation")
    calculateFactorialOf(1_000)
    println("Calculation finished!")

    emit(3)
}

private suspend fun calculateFactorialOf(number: Int): BigInteger = coroutineScope {
    var factorial = BigInteger.ONE
    for (i in 1..number) {
        factorial = factorial.multiply(BigInteger.valueOf(i.toLong()))
        ensureActive()
    }
    factorial
}