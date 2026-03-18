package com.gustavo.brilhante.coinroutine.coins.data.remote.impl

import com.gustavo.brilhante.coinroutine.appsekret.AppSecrets
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinDetailsResponseDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinPriceHistoryResponseDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinsResponseDto
import com.gustavo.brilhante.coinroutine.coins.domain.api.CoinsRemoteDataSource
import com.gustavo.brilhante.coinroutine.core.domain.Result
import com.gustavo.brilhante.coinroutine.core.domain.DataError
import com.gustavo.brilhante.coinroutine.core.network.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class KtorCoinsRemoteDataSource(
    private val httpClient: HttpClient
) : CoinsRemoteDataSource {

    override suspend fun getListOfCoins(): Result<CoinsResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("${AppSecrets.apiUrl}/coins")
        }
    }

    override suspend fun getPriceHistory(coinId: String): Result<CoinPriceHistoryResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("${AppSecrets.apiUrl}/coin/$coinId/history")
        }
    }

    override suspend fun getCoinById(coinId: String): Result<CoinDetailsResponseDto, DataError.Remote> {
        return safeCall {
            httpClient.get("${AppSecrets.apiUrl}/coin/$coinId")
        }
    }
}