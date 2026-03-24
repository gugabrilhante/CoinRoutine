package com.gustavo.brilhante.coinroutine.biometric

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import coinroutine.composeapp.generated.resources.Res
import coinroutine.composeapp.generated.resources.ic_face_id
import coinroutine.composeapp.generated.resources.ic_fingerprint
import com.gustavo.brilhante.coinroutine.PlatformType
import com.gustavo.brilhante.coinroutine.getPlatform
import org.jetbrains.compose.resources.vectorResource

val BiometricIcon: ImageVector
    @Composable
    get() = when (getPlatform().type) {
        PlatformType.ANDROID -> vectorResource(Res.drawable.ic_fingerprint)
        PlatformType.IOS -> vectorResource(Res.drawable.ic_face_id)
    }