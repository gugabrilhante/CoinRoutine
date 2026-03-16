package com.gustavo.brilhante.coinroutine.coins.domain.mapper

import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinItemDto
import com.gustavo.brilhante.coinroutine.coins.domain.coin.Coin
import com.gustavo.brilhante.coinroutine.coins.domain.model.CoinModel

fun CoinItemDto.toCoinModel() = CoinModel(
    coin = Coin(
        id = uuid,
        name = name,
        symbol = symbol,
        iconUrl = iconUrl,
    ),
    price = price,
    change = change,
)