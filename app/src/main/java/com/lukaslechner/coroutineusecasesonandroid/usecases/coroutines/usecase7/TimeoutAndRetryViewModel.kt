package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val numberOfRetries = 2
        val timeout = 1000L

        // TODO: Exercise 3
        // switch to branch "coroutine_course_full" to see solution

        // run api.getAndroidVersionFeatures(27) and api.getAndroidVersionFeatures(28) in parallel

        val oreoFeaturesDeferred = viewModelScope.async {
            retryWithTimeout(
                timeout,
                numberOfRetries
            ) {
                api.getAndroidVersionFeatures(27)
            }
        }

        val pieFeaturesDeferred = viewModelScope.async {
            retryWithTimeout(
                timeout,
                numberOfRetries
            ) {
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch {
            try {
                uiState.value = UiState.Success(listOf(oreoFeaturesDeferred, pieFeaturesDeferred).awaitAll())
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network Request failed!")
            }
        }
    }

    private suspend fun <T> retryWithTimeout(
        timeout: Long,
        numberOfRetries: Int,
        block: suspend () -> T
    ): T {
        return retry(numberOfRetries) {
            withTimeout(timeout) {
                block()
            }
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        initialDelayMillis: Long = 100,
        maxDelayMillis: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelayMillis
        repeat(numberOfRetries) {
            try {
                return block()
            } catch (e: Exception) {
                Timber.e(e)
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMillis)
        }

        return block()
    }
}
