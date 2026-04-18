package com.gustavo.brilhante.coinroutine.coins.data

import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinDetailsResponseDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinItemDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinPriceDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinPriceHistoryDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinPriceHistoryResponseDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinResponseDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinsListDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinsResponseDto
import com.gustavo.brilhante.coinroutine.coins.domain.api.CoinsRemoteDataSource
import com.gustavo.brilhante.coinroutine.core.domain.DataError
import com.gustavo.brilhante.coinroutine.core.domain.Result

class FakeCoinsRemoteDataSource : CoinsRemoteDataSource {

    var coinsListResult: Result<CoinsResponseDto, DataError.Remote> = Result.Success(
        CoinsResponseDto(data = CoinsListDto(coins = listOf(defaultCoinDto)))
    )
    var coinDetailsResult: Result<CoinDetailsResponseDto, DataError.Remote> = Result.Success(
        CoinDetailsResponseDto(data = CoinResponseDto(coin = defaultCoinDto))
    )
    var priceHistoryResult: Result<CoinPriceHistoryResponseDto, DataError.Remote> = Result.Success(
        CoinPriceHistoryResponseDto(data = CoinPriceHistoryDto(history = defaultPriceHistory))
    )

    override suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote> = coinsListResult

    override suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote> =
        priceHistoryResult

    override suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote> =
        coinDetailsResult

    companion object {
        val defaultCoinDto = CoinItemDto(
            uuid = "bitcoin",
            symbol = "BTC",
            name = "Bitcoin",
            iconUrl = "https://example.com/btc.svg",
            price = 50000.0,
            rank = 1,
            change = 2.5,
        )
        val secondCoinDto = CoinItemDto(
            uuid = "ethereum",
            symbol = "ETH",
            name = "Ethereum",
            iconUrl = "https://example.com/eth.svg",
            price = 3000.0,
            rank = 2,
            change = -1.5,
        )
        val defaultPriceHistory = listOf(
            CoinPriceDto(price = 45000.0, timestamp = 1000L),
            CoinPriceDto(price = 47000.0, timestamp = 2000L),
            CoinPriceDto(price = 50000.0, timestamp = 3000L),
        )
    }
}
