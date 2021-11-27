plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Apps.compileSdk

    defaultConfig {
        applicationId = "com.ivyclub.contact"
        minSdk = Apps.minSdk
        targetSdk = Apps.targetSdk
        versionCode = Apps.versionCode
        versionName = Apps.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        lint {
            isAbortOnError = false
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(Dep.AndroidX.core)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.material)
    implementation(Dep.AndroidX.constraintLayout)
    implementation(Dep.AndroidX.navigationUIKtx)
    implementation(Dep.AndroidX.legacySupport)
    implementation(Dep.AndroidX.navigationFragmentKtx)
    implementation(Dep.AndroidX.activityKtx)
    implementation(Dep.AndroidX.fragmentKtx)
    implementation(Dep.AndroidX.coroutine)
    implementation(Dep.AndroidX.coroutineCore)
    implementation(Dep.AndroidX.viewpager2)
    implementation(Dep.AndroidX.support)
    implementation(Dep.AndroidX.liveDataKtx)
    implementation(Dep.AndroidX.lifecycleRuntimeKtx)
    implementation(Dep.AndroidX.biometric)
    implementation(Dep.AndroidX.workRuntime)
    implementation(Dep.AndroidX.hiltWork)
    implementation(Dep.Libs.glide)
    implementation(Dep.Libs.gson)
    implementation(Dep.Libs.flexboxLayout)
    implementation(project(mapOf("path" to ":data")))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0-RC")
    kapt(Dep.AndroidX.roomCompiler)
    testImplementation(Dep.Test.jUnit)
    testImplementation("org.mockito:mockito-core:1.10.19")
    androidTestImplementation(Dep.Test.ext)
    androidTestImplementation(Dep.Test.espresso)
    implementation(Dep.Libs.hilt)
    implementation(Dep.Libs.hiltViewModel)
    implementation(Dep.Libs.indicator)
    implementation(Dep.Libs.lottie)
    kapt(Dep.Libs.hiltCompiler)
    kapt(Dep.Libs.hiltViewModelCompiler)
    kapt(Dep.Libs.hiltWorkCompiler)
    testImplementation(Dep.Test.jUnit)
    implementation ("de.svenkubiak", "jBCrypt", "0.4.1")
    implementation(project(mapOf("path" to ":data")))
    androidTestImplementation(Dep.Test.ext)
    androidTestImplementation(Dep.Test.espresso)
    androidTestImplementation(Dep.Test.hilt)
}
