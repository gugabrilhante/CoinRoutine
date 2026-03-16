package com.gustavo.brilhante.coinroutine.coins.domain.model

import com.gustavo.brilhante.coinroutine.coins.domain.coin.Coin

data class CoinModel(
    val coin: Coin,
    val price: Double,
    val change: Double,
)