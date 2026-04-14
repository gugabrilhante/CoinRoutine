package com.gustavo.brilhante.coinroutine.coins.domain.mapper

import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinItemDto
import com.gustavo.brilhante.coinroutine.coins.data.remote.dto.CoinPriceDto
import kotlin.test.Test
import kotlin.test.assertEquals

class CoinItemMapperTest {

    @Test
    fun `CoinItemDto maps to CoinModel with all fields preserved`() {
        // Arrange
        val dto = CoinItemDto(
            uuid = "bitcoin",
            name = "Bitcoin",
            symbol = "BTC",
            iconUrl = "https://example.com/btc.svg",
            price = 50000.0,
            rank = 1,
            change = 2.5,
        )

        // Act
        val model = dto.toCoinModel()

        // Assert
        assertEquals(dto.uuid, model.coin.id)
        assertEquals(dto.name, model.coin.name)
        assertEquals(dto.symbol, model.coin.symbol)
        assertEquals(dto.iconUrl, model.coin.iconUrl)
        assertEquals(dto.price, model.price)
        assertEquals(dto.change, model.change)
    }

    @Test
    fun `CoinPriceDto maps to PriceModel with correct price and timestamp`() {
        // Arrange
        val dto = CoinPriceDto(price = 48500.0, timestamp = 1700000000L)

        // Act
        val model = dto.toPriceModel()

        // Assert
        assertEquals(48500.0, model.price)
        assertEquals(1700000000L, model.timestamp)
    }

    @Test
    fun `CoinPriceDto with null price defaults to 0_0 in PriceModel`() {
        // Arrange
        val dto = CoinPriceDto(price = null, timestamp = 1700000000L)

        // Act
        val model = dto.toPriceModel()

        // Assert
        assertEquals(0.0, model.price)
        assertEquals(1700000000L, model.timestamp)
    }

    @Test
    fun `CoinItemDto with negative change maps correctly`() {
        // Arrange
        val dto = CoinItemDto(
            uuid = "ethereum",
            name = "Ethereum",
            symbol = "ETH",
            iconUrl = "https://example.com/eth.svg",
            price = 3000.0,
            rank = 2,
            change = -3.2,
        )

        // Act
        val model = dto.toCoinModel()

        // Assert
        assertEquals(-3.2, model.change)
        assertEquals("ethereum", model.coin.id)
    }
}
