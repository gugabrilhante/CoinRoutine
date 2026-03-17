package com.gustavo.brilhante.coinroutine.core.database.portifolio

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.gustavo.brilhante.coinroutine.portfolio.data.local.PortfolioCoinEntity
import com.gustavo.brilhante.coinroutine.portfolio.data.local.PortfolioDao

@Database(entities = [PortfolioCoinEntity::class], version = 1)
@ConstructedBy(PortfolioDatabaseCreator::class)
abstract class PortfolioDatabase: RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
}