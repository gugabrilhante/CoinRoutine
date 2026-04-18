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
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SellCoinUseCaseTest {

    private lateinit var useCase: SellCoinUseCase
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
        useCase = SellCoinUseCase(repository)
    }

    @Test
    fun `selling part of a holding updates the remaining amount`() = runTest {
        // Arrange: own 0.01 BTC worth $500, sell half
        val existing = portfolioCoinWith(ownedUnits = 0.01, ownedFiat = 500.0)
        repository.getPortfolioCoinResult = Result.Success(existing)
        val sellFiat = 250.0
        val price = 50000.0

        // Act
        val result = useCase.sellCoin(coin, sellFiat, price)

        // Assert
        assertIs<Result.Success<Unit>>(result)
        val remaining = repository.getSavedCoin(coin.id)
        assertNotNull(remaining)
        assertEquals(0.01 - sellFiat / price, remaining.ownedAmountInUnit, absoluteTolerance = 0.000001)
        assertEquals(500.0 - sellFiat, remaining.ownedAmountInFiat)
    }

    @Test
    fun `selling removes coin when remaining fiat value is below threshold`() = runTest {
        // Arrange: remaining fiat will be 0.50 which is below the $1 threshold
        val existing = portfolioCoinWith(ownedUnits = 0.00002, ownedFiat = 1.0)
        repository.getPortfolioCoinResult = Result.Success(existing)
        val price = 50000.0
        val sellFiat = 0.51 // leaves $0.49 remaining

        // Act
        val result = useCase.sellCoin(coin, sellFiat, price)

        // Assert
        assertIs<Result.Success<Unit>>(result)
        assertFalse(repository.hasCoin(coin.id), "Coin should have been removed from portfolio")
    }

    @Test
    fun `selling adds the sold amount to cash balance`() = runTest {
        // Arrange
        val initialBalance = 5000.0
        repository.setCashBalance(initialBalance)
        val existing = portfolioCoinWith(ownedUnits = 0.01, ownedFiat = 500.0)
        repository.getPortfolioCoinResult = Result.Success(existing)
        val sellFiat = 200.0

        // Act
        useCase.sellCoin(coin, sellFiat, price = 50000.0)

        // Assert
        assertEquals(initialBalance + sellFiat, repository.getCashBalance())
    }

    @Test
    fun `selling more units than owned returns INSUFFICIENT_FUNDS error`() = runTest {
        // Arrange: own 0.001 BTC but try to sell 0.002 BTC worth
        val existing = portfolioCoinWith(ownedUnits = 0.001, ownedFiat = 50.0)
        repository.getPortfolioCoinResult = Result.Success(existing)
        val price = 50000.0
        val sellFiat = 200.0 // would require 0.004 BTC (more than owned)

        // Act
        val result = useCase.sellCoin(coin, sellFiat, price)

        // Assert
        val error = assertIs<Result.Error<DataError>>(result)
        assertEquals(DataError.Local.INSUFFICIENT_FUNDS, error.error)
    }

    @Test
    fun `selling when coin does not exist in portfolio returns INSUFFICIENT_FUNDS error`() = runTest {
        // Arrange
        repository.getPortfolioCoinResult = Result.Success(null)

        // Act
        val result = useCase.sellCoin(coin, amountInFiat = 100.0, price = 50000.0)

        // Assert
        val error = assertIs<Result.Error<DataError>>(result)
        assertEquals(DataError.Local.INSUFFICIENT_FUNDS, error.error)
    }

    @Test
    fun `selling propagates error when fetching portfolio coin fails`() = runTest {
        // Arrange
        repository.getPortfolioCoinResult = Result.Error(DataError.Remote.NO_INTERNET)

        // Act
        val result = useCase.sellCoin(coin, amountInFiat = 100.0, price = 50000.0)

        // Assert
        val error = assertIs<Result.Error<DataError>>(result)
        assertEquals(DataError.Remote.NO_INTERNET, error.error)
    }

    private fun portfolioCoinWith(ownedUnits: Double, ownedFiat: Double) = PortfolioCoinModel(
        coin = coin,
        ownedAmountInUnit = ownedUnits,
        ownedAmountInFiat = ownedFiat,
        performancePercent = 0.0,
        averagePurchasePrice = ownedFiat / ownedUnits,
    )
}

private fun assertEquals(expected: Double, actual: Double, absoluteTolerance: Double) {
    assertTrue(
        kotlin.math.abs(expected - actual) <= absoluteTolerance,
        "Expected $expected but was $actual (tolerance $absoluteTolerance)"
    )
}
