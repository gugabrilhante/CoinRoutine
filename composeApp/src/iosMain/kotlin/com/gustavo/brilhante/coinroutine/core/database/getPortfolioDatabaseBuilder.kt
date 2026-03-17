package com.gustavo.brilhante.coinroutine.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.gustavo.brilhante.coinroutine.core.database.portifolio.PortfolioDatabase
import platform.Foundation.NSHomeDirectory

fun getPortfolioDatabaseBuilder(): RoomDatabase.Builder<PortfolioDatabase> {
    val dbFile = NSHomeDirectory() + "/portfolio.db"
    return Room.databaseBuilder<PortfolioDatabase>(
        name = dbFile,
    )
}