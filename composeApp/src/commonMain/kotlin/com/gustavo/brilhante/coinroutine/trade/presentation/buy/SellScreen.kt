package com.gustavo.brilhante.coinroutine.trade.presentation.buy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gustavo.brilhante.coinroutine.trade.presentation.common.TradeScreen
import com.gustavo.brilhante.coinroutine.trade.presentation.common.TradeType
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SellScreen(
    coinId: String,
    navigateToPortfolio: () -> Unit,
) {
    val viewModel = koinViewModel<SellViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    //TODO: handle coinId

    TradeScreen(
        state = state,
        tradeType = TradeType.SELL,
        onAmountChange = viewModel::onAmountChanged,
        onSubmitClicked = viewModel::onSellClicked
    )
}