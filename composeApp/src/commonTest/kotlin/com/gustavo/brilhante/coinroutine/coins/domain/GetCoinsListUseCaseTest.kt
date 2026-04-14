package com.gustavo.brilhante.coinroutine.coins.domain

import com.gustavo.brilhante.coinroutine.coins.data.FakeCoinsRemoteDataSource
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinsListDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinsResponseDto
import com.gustavo.brilhante.coinroutine.coins.domain.model.CoinModel
import com.gustavo.brilhante.coinroutine.core.domain.DataError
import com.gustavo.brilhante.coinroutine.core.domain.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetCoinsListUseCaseTest {

    private lateinit var useCase: GetCoinsListUseCase
    private lateinit var dataSource: FakeCoinsRemoteDataSource

    @BeforeTest
    fun setup() {
        dataSource = FakeCoinsRemoteDataSource()
        useCase = GetCoinsListUseCase(dataSource)
    }

    @Test
    fun `returns list of mapped coin models on success`() = runTest {
        // Arrange
        val coinDto = FakeCoinsRemoteDataSource.defaultCoinDto
        dataSource.coinsListResult = Result.Success(
            CoinsResponseDto(data = CoinsListDto(coins = listOf(coinDto)))
        )

        // Act
        val result = useCase.execute()

        // Assert
        val success = assertIs<Result.Success<List<CoinModel>>>(result)
        val coins = success.data
        assertEquals(1, coins.size)
        assertEquals(coinDto.uuid, coins.first().coin.id)
        assertEquals(coinDto.name, coins.first().coin.name)
        assertEquals(coinDto.symbol, coins.first().coin.symbol)
        assertEquals(coinDto.price, coins.first().price)
        assertEquals(coinDto.change, coins.first().change)
    }

    @Test
    fun `returns empty list when server returns no coins`() = runTest {
        // Arrange
        dataSource.coinsListResult = Result.Success(
            CoinsResponseDto(data = CoinsListDto(coins = emptyList()))
        )

        // Act
        val result = useCase.execute()

        // Assert
        val success = assertIs<Result.Success<List<CoinModel>>>(result)
        assertEquals(0, success.data.size)
    }

    @Test
    fun `maps multiple coins preserving their order`() = runTest {
        // Arrange
        val dto1 = FakeCoinsRemoteDataSource.defaultCoinDto
        val dto2 = FakeCoinsRemoteDataSource.secondCoinDto
        dataSource.coinsListResult = Result.Success(
            CoinsResponseDto(data = CoinsListDto(coins = listOf(dto1, dto2)))
        )

        // Act
        val result = useCase.execute()

        // Assert
        val success = assertIs<Result.Success<List<CoinModel>>>(result)
        assertEquals(2, success.data.size)
        assertEquals(dto1.uuid, success.data[0].coin.id)
        assertEquals(dto2.uuid, success.data[1].coin.id)
    }

    @Test
    fun `propagates remote error on network failure`() = runTest {
        // Arrange
        dataSource.coinsListResult = Result.Error(DataError.Remote.NO_INTERNET)

        // Act
        val result = useCase.execute()

        // Assert
        val error = assertIs<Result.Error<DataError.Remote>>(result)
        assertEquals(DataError.Remote.NO_INTERNET, error.error)
    }

    @Test
    fun `propagates server error`() = runTest {
        // Arrange
        dataSource.coinsListResult = Result.Error(DataError.Remote.SERVER)

        // Act
        val result = useCase.execute()

        // Assert
        val error = assertIs<Result.Error<DataError.Remote>>(result)
        assertEquals(DataError.Remote.SERVER, error.error)
    }
}
