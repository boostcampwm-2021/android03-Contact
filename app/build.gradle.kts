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
    implementation(Dep.Libs.hilt)
    implementation(Dep.Libs.hiltViewModel)
    implementation(Dep.Libs.indicator)
    implementation(Dep.Libs.lottie)
    implementation(Dep.Libs.jBCrypt)
    implementation(project(mapOf("path" to ":data")))
    kapt(Dep.AndroidX.roomCompiler)
    kapt(Dep.Libs.hiltCompiler)
    kapt(Dep.Libs.hiltViewModelCompiler)
    kapt(Dep.Libs.hiltWorkCompiler)
    testImplementation(Dep.Test.jUnit)
    testImplementation(Dep.Test.mockito)
    testImplementation(Dep.Test.coroutines)
    testImplementation(Dep.Test.mockitoInline)
    androidTestImplementation(Dep.Test.ext)
    androidTestImplementation(Dep.Test.espresso)
    androidTestImplementation(Dep.Test.hilt)
}
