package com.teEcclesia.util

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import com.teEcclesia.home.api.HomeRoute
import com.teEcclesia.identity.api.LoginRoute
import com.teEcclesia.identity.api.ProfileRoute
import com.teEcclesia.identity.api.SignUpRoute
import com.teEcclesia.identity.api.VerifyPhoneRoute
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

fun buildNavigationSerializerConfig(): SavedStateConfiguration = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(LoginRoute::class, LoginRoute.serializer())
            subclass(SignUpRoute::class, SignUpRoute.serializer())
            subclass(VerifyPhoneRoute::class, VerifyPhoneRoute.serializer())
            subclass(ProfileRoute::class, ProfileRoute.serializer())
            subclass(HomeRoute::class, HomeRoute.serializer())
        }
    }
}
