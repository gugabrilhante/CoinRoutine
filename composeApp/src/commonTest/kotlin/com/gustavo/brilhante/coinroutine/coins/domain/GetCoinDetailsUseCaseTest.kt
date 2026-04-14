package com.gustavo.brilhante.coinroutine.coins.domain

import com.gustavo.brilhante.coinroutine.coins.data.FakeCoinsRemoteDataSource
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinDetailsResponseDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinResponseDto
import com.gustavo.brilhante.coinroutine.coins.domain.model.CoinModel
import com.gustavo.brilhante.coinroutine.core.domain.DataError
import com.gustavo.brilhante.coinroutine.core.domain.Result
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class GetCoinDetailsUseCaseTest {

    private lateinit var useCase: GetCoinDetailsUseCase
    private lateinit var dataSource: FakeCoinsRemoteDataSource

    @BeforeTest
    fun setup() {
        dataSource = FakeCoinsRemoteDataSource()
        useCase = GetCoinDetailsUseCase(dataSource)
    }

    @Test
    fun `returns mapped coin model with all fields on success`() = runTest {
        // Arrange
        val coinDto = FakeCoinsRemoteDataSource.defaultCoinDto
        dataSource.coinDetailsResult = Result.Success(
            CoinDetailsResponseDto(data = CoinResponseDto(coin = coinDto))
        )

        // Act
        val result = useCase.execute(coinId = coinDto.uuid)

        // Assert
        val success = assertIs<Result.Success<CoinModel>>(result)
        val model = success.data
        assertEquals(coinDto.uuid, model.coin.id)
        assertEquals(coinDto.name, model.coin.name)
        assertEquals(coinDto.symbol, model.coin.symbol)
        assertEquals(coinDto.iconUrl, model.coin.iconUrl)
        assertEquals(coinDto.price, model.price)
        assertEquals(coinDto.change, model.change)
    }

    @Test
    fun `propagates remote error on request timeout`() = runTest {
        // Arrange
        dataSource.coinDetailsResult = Result.Error(DataError.Remote.REQUEST_TIMEOUT)

        // Act
        val result = useCase.execute(coinId = "bitcoin")

        // Assert
        val error = assertIs<Result.Error<DataError.Remote>>(result)
        assertEquals(DataError.Remote.REQUEST_TIMEOUT, error.error)
    }

    @Test
    fun `propagates serialization error when response is malformed`() = runTest {
        // Arrange
        dataSource.coinDetailsResult = Result.Error(DataError.Remote.SERIALIZATION)

        // Act
        val result = useCase.execute(coinId = "bitcoin")

        // Assert
        val error = assertIs<Result.Error<DataError.Remote>>(result)
        assertEquals(DataError.Remote.SERIALIZATION, error.error)
    }
}
