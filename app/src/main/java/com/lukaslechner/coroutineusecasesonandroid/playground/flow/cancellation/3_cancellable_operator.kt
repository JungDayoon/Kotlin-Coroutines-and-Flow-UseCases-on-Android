package com.lukaslechner.coroutineusecasesonandroid.playground.flow.cancellation

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

suspend fun main() {
    val scope = CoroutineScope(EmptyCoroutineContext)
    scope.launch {
        flowOf(1, 2, 3)
            .onCompletion { cause: Throwable? ->
                if (cause is CancellationException) {
                    println("Flow got cancelled")
                }
            }.cancellable()
//            .onEach {
//                println("Receive $it in onEach")
////                if (!currentCoroutineContext().job.isActive) {
////                    throw CancellationException()
////                }
//                ensureActive()
//            }
            .collect {
                println("Collected $it")
                if (it == 2) {
                    cancel()
                }
            }
    }.join()
}
/**
 * 결과
 * collected 1
 * collected 2
 * collected 3 (취소되지 않고 collect 됨)
 *
 * 이유
 * flowOf builder는 내부적으로 collecting coroutine이 active한지 체크하지 않음
 * -> onEach{} 에서 active한지 체크한 후에 CancellationException을 throw 해주어야 함
 * -> 이를 ensureActive() 한줄로 간단히 사용 가능
 * -> onEach{} 내부에서 ensureActive() 체크를 cancellable()로 간단히 사용 가능
 */