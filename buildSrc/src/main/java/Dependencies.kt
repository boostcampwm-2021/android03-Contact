object Apps {
    const val compileSdk = 31
    const val minSdk = 21
    const val targetSdk = 31
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Versions {
    const val gradle = "7.0.3"
    const val kotlin = "1.5.31"
    const val appcompat = "1.3.1"
    const val junit = "4.13.2"
    const val navigation = "2.4.0-alpha10"
    const val activity = "1.3.1"
    const val fragment = "1.4.0-alpha10"
    const val room = "2.3.0"
    const val hilt = "2.38.1"
    const val hiltViewModel = "1.0.0-alpha03"
    const val flexbox = "3.0.0"
    const val work = "2.7.1"
    const val hiltWork = "1.0.0-alpha03"
    const val lifeCycle = "2.4.0"
    const val crashlyticsGradle = "2.7.1"
    const val firebaseBom = "28.4.0"
    const val googleServices = "4.3.10"
}

object Dep {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.gradle}"
    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.0"
        const val core = "androidx.core:core-ktx:1.6.0"
        const val material = "com.google.android.material:material:1.4.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.2"
        const val navigationUIKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val legacySupport = "androidx.legacy:legacy-support-v4:1.0.0"
        const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val activityKtx = "androidx.activity:activity-ktx:${Versions.activity}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
        const val viewpager2 = "androidx.viewpager2:viewpager2:1.0.0"
        const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val support = "com.android.support:support-compat:28.0.0"
        const val sharedPreference = "androidx.preference:preference-ktx:1.1.0"
        const val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val workRuntime = "androidx.work:work-runtime-ktx:${Versions.work}"
        const val hiltWork = "androidx.hilt:hilt-work:${Versions.hiltWork}"
        const val biometric = "androidx.biometric:biometric:1.2.0-alpha04"
        const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifeCycle}"
        const val navigationSafeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    }

    object Kotlin {
        const val gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31"
        const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9"
        const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2"
        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    }

    object Test {
        const val jUnit = "junit:junit:${Versions.junit}"
        const val ext = "androidx.test.ext:junit:1.1.3"
        const val espresso = "androidx.test.espresso:espresso-core:3.4.0"
        const val room = "androidx.room:room-testing:${Versions.room}"
        const val hilt = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0-RC"
        const val mockito = "org.mockito:mockito-core:3.9.0"
        const val mockitoInline = "org.mockito:mockito-inline:3.5.13"
        const val core = "androidx.test:core-ktx:1.4.0"
        const val contrib = "androidx.test.espresso:espresso-contrib:3.1.0"
    }

    object Libs {
        const val glide = "com.github.bumptech.glide:glide:4.12.0"
        const val gson = "com.google.code.gson:gson:2.8.8"
        const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
        const val hiltViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltViewModel}"
        const val hiltViewModelCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltViewModel}"
        const val hiltWorkCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltWork}"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
        const val flexboxLayout = "com.google.android.flexbox:flexbox:${Versions.flexbox}"
        const val indicator = "com.tbuonomo:dotsindicator:4.2"
        const val lottie = "com.airbnb.android:lottie:4.2.1"
        const val jBCrypt = "de.svenkubiak:jBCrypt:0.4.1"
        const val ossLicensesProject = "com.google.android.gms:oss-licenses-plugin:0.10.4"
        const val ossLicensesLibrary = "com.google.android.gms:play-services-oss-licenses:17.0.0"
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.7"
    }

    object Firebase {
        const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
        const val crashlyticsGradle = "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlyticsGradle}"
        const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
        const val crashlyticsKtx = "com.google.firebase:firebase-crashlytics-ktx"
        const val analyticsKtx = "com.google.firebase:firebase-analytics-ktx"
    }
}
