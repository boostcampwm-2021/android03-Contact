plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Apps.compileSdk
}

dependencies {
    implementation(Dep.AndroidX.core)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.material)
    api(Dep.AndroidX.roomRuntime)
    implementation(Dep.AndroidX.sharedPreference)
    implementation(Dep.Libs.gson)
    implementation(Dep.Libs.hilt)
    implementation(Dep.Kotlin.coroutine)
    implementation(Dep.Kotlin.coroutineCore)
    implementation(Dep.AndroidX.roomKtx)
    androidTestImplementation(Dep.Test.ext)
    androidTestImplementation(Dep.Test.espresso)
    testImplementation(Dep.Test.jUnit)
    androidTestImplementation(Dep.Test.room)
    androidTestImplementation(Dep.Test.hilt)
    kapt(Dep.AndroidX.roomCompiler)
    kapt(Dep.Libs.hiltCompiler)
    annotationProcessor(Dep.AndroidX.roomCompiler)
}
