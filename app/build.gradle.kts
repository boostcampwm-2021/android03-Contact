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
    implementation(Dep.AndroidX.roomRuntime)
    implementation(Dep.AndroidX.liveDataKtx)
    implementation(Dep.Libs.glide)
    implementation(Dep.Libs.gson)
    implementation(Dep.Libs.flexboxLayout)
    implementation(project(mapOf("path" to ":data")))
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt(Dep.AndroidX.roomCompiler)
    testImplementation(Dep.Test.jUnit)
    androidTestImplementation(Dep.Test.ext)
    androidTestImplementation(Dep.Test.espresso)
    implementation(Dep.Libs.hilt)
    kapt(Dep.Libs.hiltCompiler)
    implementation(Dep.Libs.hiltViewModel)
    kapt(Dep.Libs.hiltViewModelCompiler)
    implementation(Dep.AndroidX.workRuntime)
    implementation(Dep.AndroidX.hiltWork)
    kapt(Dep.Libs.hiltWorkCompiler)

    implementation(project(":data"))
}
