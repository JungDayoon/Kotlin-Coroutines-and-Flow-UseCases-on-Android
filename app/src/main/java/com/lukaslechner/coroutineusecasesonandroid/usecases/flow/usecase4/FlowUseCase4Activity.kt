package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase4

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lukaslechner.coroutineusecasesonandroid.base.BaseActivity
import com.lukaslechner.coroutineusecasesonandroid.base.flowUseCase4Description
import com.lukaslechner.coroutineusecasesonandroid.databinding.ActivityFlowUsecase1Binding
import com.lukaslechner.coroutineusecasesonandroid.utils.setGone
import com.lukaslechner.coroutineusecasesonandroid.utils.setVisible
import com.lukaslechner.coroutineusecasesonandroid.utils.toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class FlowUseCase4Activity : BaseActivity() {

    private val binding by lazy { ActivityFlowUsecase1Binding.inflate(layoutInflater) }
    private val adapter = StockAdapter()

    private val viewModel: FlowUseCase4ViewModel by viewModels {
        ViewModelFactory(NetworkStockPriceDataSource(mockApi(applicationContext)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentStockPriceAsFlow.collect { uiState ->
                    render(uiState)
                }
            }
        }

//        lifecycleScope.launch {
//            viewModel.currentStockPriceAsFlow
//                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collect { uiState ->
//                    render(uiState)
//                }
//        }

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.currentStockPriceAsFlow.collect { uiState ->
//                    render(uiState)
//                }
//
//                // 하나의 lifecycleScope에서 여러 flow를 collect할 수는 없음
//                // 위에 있는 flow에서 collect가 완료될 때까지 suspend하고 있기 때문에 아래에 있는 flow가 collect될 수 없음
//                // 각각 launch시 문제 해결 가능
//                viewModel.currentStockPriceAsFlow.collect { uiState ->
//                    render(uiState)
//                }
//            }
//        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentStockPriceAsFlow.collect { uiState ->
                        render(uiState)
                    }
                }

                launch {
                    viewModel.currentStockPriceAsFlow.collect { uiState ->
                        render(uiState)
                    }
                }
            }
        }
    }

    private fun render(uiState: UiState) {
        when (uiState) {
            is UiState.Loading -> {
                binding.progressBar.setVisible()
                binding.recyclerView.setGone()
            }
            is UiState.Success -> {
                binding.recyclerView.setVisible()
                binding.lastUpdateTime.text =
                    "lastUpdateTime: ${LocalDateTime.now().toString(DateTimeFormat.fullTime())}"
                adapter.stockList = uiState.stockList
                binding.progressBar.setGone()
            }
            is UiState.Error -> {
                toast(uiState.message)
                binding.progressBar.setGone()
            }
        }
    }

    override fun getToolbarTitle() = flowUseCase4Description
}