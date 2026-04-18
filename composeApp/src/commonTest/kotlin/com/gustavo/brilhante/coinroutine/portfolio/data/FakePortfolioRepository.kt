package com.gustavo.brilhante.coinroutine.portfolio.data

import com.gustavo.brilhante.coinroutine.coins.domain.coin.Coin
import com.gustavo.brilhante.coinroutine.core.domain.DataError
import com.gustavo.brilhante.coinroutine.core.domain.EmptyResult
import com.gustavo.brilhante.coinroutine.core.domain.Result
import com.gustavo.brilhante.coinroutine.portfolio.domain.PortfolioCoinModel
import com.gustavo.brilhante.coinroutine.portfolio.domain.PortfolioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePortfolioRepository : PortfolioRepository {

    private val _data = MutableStateFlow<Result<List<PortfolioCoinModel>, DataError.Remote>>(
        Result.Success(emptyList())
    )
    private val _cashBalance = MutableStateFlow(cashBalance)
    private val _portfolioValue = MutableStateFlow(portfolioValue)
    private val coinMap = mutableMapOf<String, PortfolioCoinModel>()

    /** Configure the result returned by [getPortfolioCoin] in tests. */
    var getPortfolioCoinResult: Result<PortfolioCoinModel?, DataError.Remote> = Result.Success(null)

    override suspend fun initializeBalance() {
        // no-op
    }

    override fun allPortfolioCoinsFlow(): Flow<Result<List<PortfolioCoinModel>, DataError.Remote>> {
        return _data.asStateFlow()
    }

    override suspend fun getPortfolioCoin(coinId: String): Result<PortfolioCoinModel?, DataError.Remote> {
        return getPortfolioCoinResult
    }

    override suspend fun savePortfolioCoin(portfolioCoin: PortfolioCoinModel): EmptyResult<DataError.Local> {
        coinMap[portfolioCoin.coin.id] = portfolioCoin
        val coins = coinMap.values.toList()
        _portfolioValue.value = coins.sumOf { it.ownedAmountInFiat }
        _data.value = Result.Success(coins)
        return Result.Success(Unit)
    }

    override suspend fun removeCoinFromPortfolio(coinId: String) {
        coinMap.remove(coinId)
        val coins = coinMap.values.toList()
        _portfolioValue.value = coins.sumOf { it.ownedAmountInFiat }
        _data.update { Result.Success(coins) }
    }

    override fun calculateTotalPortfolioValue(): Flow<Result<Double, DataError.Remote>> {
        return _portfolioValue.map { Result.Success(it) }
    }

    override fun totalBalanceFlow(): Flow<Result<Double, DataError.Remote>> {
        return _cashBalance.combine(_portfolioValue) { cashBalance, portfolioValue ->
            cashBalance + portfolioValue
        }.map { Result.Success(it) }
    }

    override fun cashBalanceFlow(): Flow<Double> = _cashBalance.asStateFlow()

    override suspend fun updateCashBalance(newBalance: Double) {
        _cashBalance.value = newBalance
    }

    fun simulateError() {
        _data.value = Result.Error(DataError.Remote.SERVER)
    }

    /** Helper for asserting cash balance changes in tests. */
    fun getCashBalance(): Double = _cashBalance.value

    /** Helper for asserting a coin was saved/updated with specific values. */
    fun getSavedCoin(coinId: String): PortfolioCoinModel? = coinMap[coinId]

    /** Helper for checking whether a coin was removed from the portfolio. */
    fun hasCoin(coinId: String): Boolean = coinMap.containsKey(coinId)

    /** Helper for seeding cash balance in tests. */
    fun setCashBalance(amount: Double) {
        _cashBalance.value = amount
    }

    companion object {
        val fakeCoin = Coin(
            id = "fakeId",
            name = "Fake Coin",
            symbol = "FAKE",
            iconUrl = "https://fake.url/fake.png"
        )
        val portfolioCoin = PortfolioCoinModel(
            coin = fakeCoin,
            ownedAmountInUnit = 1000.0,
            ownedAmountInFiat = 3000.0,
            performancePercent = 10.0,
            averagePurchasePrice = 10.0,
        )
        val cashBalance = 10000.0
        val portfolioValue = 0.0
    }
}