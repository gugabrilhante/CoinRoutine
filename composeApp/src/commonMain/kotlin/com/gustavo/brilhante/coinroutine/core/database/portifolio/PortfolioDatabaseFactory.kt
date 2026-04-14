package com.gustavo.brilhante.coinroutine.core.database.portifolio

import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Suppress("KotlinNoActualForExpect")
expect object PortfolioDatabaseCreator : RoomDatabaseConstructor<PortfolioDatabase>{
    override fun initialize(): PortfolioDatabase
}

val MIGRATIONS = arrayOf(
    object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL("CREATE TABLE IF NOT EXISTS `UserBalanceEntity` (`id` INTEGER NOT NULL, `cashBalance` REAL NOT NULL, PRIMARY KEY(`id`))")
        }
    }
)

fun getPortfolioDatabase(
    builder: RoomDatabase.Builder<PortfolioDatabase>
): PortfolioDatabase {
    return builder
        .addMigrations(*MIGRATIONS)
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}