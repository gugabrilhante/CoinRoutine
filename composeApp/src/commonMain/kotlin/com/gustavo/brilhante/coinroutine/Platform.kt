package com.gustavo.brilhante.coinroutine

interface Platform {
    val name: String
    val type: PlatformType
}

enum class PlatformType{
    ANDROID,
    IOS
}

expect fun getPlatform(): Platform