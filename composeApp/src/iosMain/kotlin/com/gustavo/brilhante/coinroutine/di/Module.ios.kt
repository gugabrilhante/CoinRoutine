package com.gustavo.brilhante.coinroutine.di

import androidx.room.RoomDatabase
import com.gustavo.brilhante.coinroutine.core.database.getPortfolioDatabaseBuilder
import com.gustavo.brilhante.coinroutine.core.database.portifolio.PortfolioDatabase
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    single<HttpClientEngine> { Darwin.create() }
    singleOf(::getPortfolioDatabaseBuilder).bind<RoomDatabase.Builder<PortfolioDatabase>>()
}

