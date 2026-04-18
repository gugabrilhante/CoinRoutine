package com.gustavo.brilhante.coinroutine.coins.domain

import com.gustavo.brilhante.coinroutine.coins.data.FakeCoinsRemoteDataSource
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinPriceDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinPriceHistoryDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinPriceHistoryResponseDto
import com.gustavo.brilhante.coinroutine.coins.domain.model.PriceModel
import com.gustavo.brilhante.coinroutine.core.domain.DataError
import com.gustavo.brilhante.coinroutine.core.domain.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetCoinPriceHistoryUseCaseTest {

    private lateinit var useCase: GetCoinPriceHistoryUseCase
    private lateinit var dataSource: FakeCoinsRemoteDataSource

    @BeforeTest
    fun setup() {
        dataSource = FakeCoinsRemoteDataSource()
        useCase = GetCoinPriceHistoryUseCase(dataSource)
    }

    @Test
    fun `returns mapped price history on success`() = runTest {
        // Arrange
        val history = listOf(
            CoinPriceDto(price = 45000.0, timestamp = 1000L),
            CoinPriceDto(price = 47000.0, timestamp = 2000L),
        )
        dataSource.priceHistoryResult = Result.Success(
            CoinPriceHistoryResponseDto(data = CoinPriceHistoryDto(history = history))
        )

        // Act
        val result = useCase.execute(coinId = "bitcoin")

        // Assert
        val success = assertIs<Result.Success<List<PriceModel>>>(result)
        assertEquals(2, success.data.size)
        assertEquals(45000.0, success.data[0].price)
        assertEquals(1000L, success.data[0].timestamp)
        assertEquals(47000.0, success.data[1].price)
        assertEquals(2000L, success.data[1].timestamp)
    }

    @Test
    fun `maps null price values to 0_0`() = runTest {
        // Arrange
        dataSource.priceHistoryResult = Result.Success(
            CoinPriceHistoryResponseDto(
                data = CoinPriceHistoryDto(
                    history = listOf(CoinPriceDto(price = null, timestamp = 1000L))
                )
            )
        )

        // Act
        val result = useCase.execute(coinId = "bitcoin")

        // Assert
        val success = assertIs<Result.Success<List<PriceModel>>>(result)
        assertEquals(0.0, success.data.first().price)
    }

    @Test
    fun `returns empty list when history is empty`() = runTest {
        // Arrange
        dataSource.priceHistoryResult = Result.Success(
            CoinPriceHistoryResponseDto(data = CoinPriceHistoryDto(history = emptyList()))
        )

        // Act
        val result = useCase.execute(coinId = "bitcoin")

        // Assert
        val success = assertIs<Result.Success<List<PriceModel>>>(result)
        assertEquals(0, success.data.size)
    }

    @Test
    fun `propagates remote error on failure`() = runTest {
        // Arrange
        dataSource.priceHistoryResult = Result.Error(DataError.Remote.TOO_MANY_REQUESTS)

        // Act
        val result = useCase.execute(coinId = "bitcoin")

        // Assert
        val error = assertIs<Result.Error<DataError.Remote>>(result)
        assertEquals(DataError.Remote.TOO_MANY_REQUESTS, error.error)
    }
}
