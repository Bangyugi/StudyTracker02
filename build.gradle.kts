plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // 2 plugin mới dành cho Pokedex
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false

    id("com.google.gms.google-services") version "4.5.0" apply false
    id("com.google.firebase.crashlytics") version "3.0.7" apply false
}