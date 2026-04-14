package com.gustavo.brilhante.coinroutine.trade.domain

import com.gustavo.brilhante.coinroutine.coins.domain.coin.Coin
import com.gustavo.brilhante.coinroutine.core.domain.DataError
import com.gustavo.brilhante.coinroutine.core.domain.Result
import com.gustavo.brilhante.coinroutine.portfolio.data.FakePortfolioRepository
import com.gustavo.brilhante.coinroutine.portfolio.domain.PortfolioCoinModel
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BuyCoinUseCaseTest {

    private lateinit var useCase: BuyCoinUseCase
    private lateinit var repository: FakePortfolioRepository

    private val coin = Coin(
        id = "bitcoin",
        name = "Bitcoin",
        symbol = "BTC",
        iconUrl = "https://example.com/btc.svg"
    )

    @BeforeTest
    fun setup() {
        repository = FakePortfolioRepository()
        useCase = BuyCoinUseCase(repository)
    }

    @Test
    fun `buying a new coin saves it with correct amount and price`() = runTest {
        // Arrange
        val amountInFiat = 500.0
        val price = 50000.0
        repository.getPortfolioCoinResult = Result.Success(null)

        // Act
        val result = useCase.buyCoin(coin, amountInFiat, price)

        // Assert: result is successful
        assertIs<Result.Success<Unit>>(result)
        val saved = repository.getSavedCoin(coin.id)
        assertNotNull(saved)
        assertEquals(amountInFiat / price, saved.ownedAmountInUnit)
        assertEquals(amountInFiat, saved.ownedAmountInFiat)
        assertEquals(price, saved.averagePurchasePrice)
    }

    @Test
    fun `buying an existing coin computes a weighted average purchase price`() = runTest {
        // Arrange
        val existingCoin = PortfolioCoinModel(
            coin = coin,
            ownedAmountInUnit = 0.01,   // bought 0.01 BTC
            ownedAmountInFiat = 400.0,   // at average $40,000
            performancePercent = 0.0,
            averagePurchasePrice = 40000.0,
        )
        repository.getPortfolioCoinResult = Result.Success(existingCoin)
        val additionalFiat = 500.0
        val currentPrice = 50000.0

        // Act
        val result = useCase.buyCoin(coin, additionalFiat, currentPrice)

        // Assert
        assertIs<Result.Success<Unit>>(result)
        val saved = repository.getSavedCoin(coin.id)
        assertNotNull(saved)
        val expectedUnit = existingCoin.ownedAmountInUnit + additionalFiat / currentPrice
        val expectedFiat = existingCoin.ownedAmountInFiat + additionalFiat
        val expectedAvgPrice = expectedFiat / expectedUnit
        assertEquals(expectedUnit, saved.ownedAmountInUnit, absoluteTolerance = 0.000001)
        assertEquals(expectedFiat, saved.ownedAmountInFiat)
        assertEquals(expectedAvgPrice, saved.averagePurchasePrice, absoluteTolerance = 0.01)
    }

    @Test
    fun `buying when cash balance is insufficient returns INSUFFICIENT_FUNDS error`() = runTest {
        // Arrange
        repository.setCashBalance(100.0)
        repository.getPortfolioCoinResult = Result.Success(null)

        // Act
        val result = useCase.buyCoin(coin, amountInFiat = 200.0, price = 50000.0)

        // Assert
        val error = assertIs<Result.Error<DataError>>(result)
        assertEquals(DataError.Local.INSUFFICIENT_FUNDS, error.error)
        assertNull(repository.getSavedCoin(coin.id))
    }

    @Test
    fun `buying deducts the purchase amount from cash balance`() = runTest {
        // Arrange
        val initialBalance = 10000.0
        val amountInFiat = 500.0
        repository.setCashBalance(initialBalance)
        repository.getPortfolioCoinResult = Result.Success(null)

        // Act
        useCase.buyCoin(coin, amountInFiat, price = 50000.0)

        // Assert
        assertEquals(initialBalance - amountInFiat, repository.getCashBalance())
    }

    @Test
    fun `buying propagates error when fetching existing portfolio coin fails`() = runTest {
        // Arrange
        repository.getPortfolioCoinResult = Result.Error(DataError.Remote.SERVER)

        // Act
        val result = useCase.buyCoin(coin, amountInFiat = 100.0, price = 50000.0)

        // Assert
        val error = assertIs<Result.Error<DataError>>(result)
        assertEquals(DataError.Remote.SERVER, error.error)
    }
}

private fun assertEquals(expected: Double, actual: Double, absoluteTolerance: Double) {
    assertTrue(
        kotlin.math.abs(expected - actual) <= absoluteTolerance,
        "Expected $expected but was $actual (tolerance $absoluteTolerance)"
    )
}
