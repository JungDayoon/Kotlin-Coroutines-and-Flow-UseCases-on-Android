package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PerformSingleNetworkRequestViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performSingleNetworkRequest() {
        uiState.value = UiState.Loading

        val job = viewModelScope.launch(Dispatchers.Main) {
            Timber.d("I am the first statement in the coroutine")
            try {
                val recentAndroidVersions = mockApi.getRecentAndroidVersions()
                uiState.value = UiState.Success(recentAndroidVersions)
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("network request failed!")
            }

        }

        Timber.d("I am the first statement after launching the coroutine")

        job.invokeOnCompletion {
            if (it is CancellationException) {
                Timber.d("Coroutine was cancelled")
            }
        }
    }
}
