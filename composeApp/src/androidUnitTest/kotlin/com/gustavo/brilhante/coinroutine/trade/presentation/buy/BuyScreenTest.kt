package com.gustavo.brilhante.coinroutine.trade.presentation.buy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gustavo.brilhante.coinroutine.trade.presentation.common.TradeScreen
import com.gustavo.brilhante.coinroutine.trade.presentation.common.TradeState
import com.gustavo.brilhante.coinroutine.trade.presentation.common.TradeType
import com.gustavo.brilhante.coinroutine.trade.presentation.common.UiTradeCoinItem
import org.junit.After
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
class BuyScreenTest {

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkSubmitButtonLabelChangesWithTradeType() = runComposeUiTest {
        val state = TradeState(
            coin = UiTradeCoinItem(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "url",
                price = 50000.0
            )
        )
        var tradeType by mutableStateOf(TradeType.BUY)

        setContent {
            TradeScreen(
                state = state,
                tradeType = tradeType,
                onAmountChange = {},
                onSubmitClicked = {}
            )
        }

        onNodeWithTag("trade_type_label").assertIsDisplayed()
        onNodeWithTag("trade_submit_button").assertIsDisplayed()

        tradeType = TradeType.SELL

        onNodeWithTag("trade_type_label").assertIsDisplayed()
        onNodeWithTag("trade_submit_button").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun checkIfCoinNameShowProperlyInBuy() = runComposeUiTest {
        val state = TradeState(
            coin = UiTradeCoinItem(
                id = "bitcoin",
                name = "Bitcoin",
                symbol = "BTC",
                iconUrl = "url",
                price = 50000.0
            )
        )

        setContent {
            TradeScreen(
                state = state,
                tradeType = TradeType.BUY,
                onAmountChange = {},
                onSubmitClicked = {}
            )
        }

        onNodeWithTag("trade_screen_coin_name").assertIsDisplayed()
    }
}