package com.gustavo.brilhante.coinroutine.portfolio.domain

import com.gustavo.brilhante.coinroutine.coins.domain.coin.Coin

data class PortfolioCoinModel(
    val coin: Coin,
    val performancePercent: Double,
    val averagePurchasePrice: Double,
    val ownedAmountInUnit: Double,
    val ownedAmountInFiat: Double,
)