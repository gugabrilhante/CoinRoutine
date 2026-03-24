package com.gustavo.brilhante.coinroutine

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val type: PlatformType get() = PlatformType.ANDROID
}

actual fun getPlatform(): Platform = AndroidPlatform()