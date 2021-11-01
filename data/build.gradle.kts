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
    implementation(Dep.AndroidX.roomRuntime)
    implementation(Dep.Libs.gson)
    implementation(Dep.Libs.hilt)
    androidTestImplementation(Dep.Test.ext)
    androidTestImplementation(Dep.Test.espresso)
    testImplementation(Dep.Test.jUnit)
    kapt(Dep.AndroidX.roomCompiler)
    kapt(Dep.Libs.hiltCompiler)
    annotationProcessor(Dep.AndroidX.roomCompiler)
}
