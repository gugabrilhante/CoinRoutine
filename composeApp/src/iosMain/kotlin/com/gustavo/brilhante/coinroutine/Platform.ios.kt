package com.gustavo.brilhante.coinroutine

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val type: PlatformType get() = PlatformType.IOS
}

actual fun getPlatform(): Platform = IOSPlatform()