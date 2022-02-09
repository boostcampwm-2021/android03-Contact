buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dep.androidGradlePlugin)
        classpath(Dep.Kotlin.gradle)
        classpath(Dep.Libs.hiltGradlePlugin)
        classpath(Dep.AndroidX.navigationSafeArgs)
        classpath(Dep.Firebase.googleServices)
        classpath(Dep.Firebase.crashlyticsGradle)
        classpath(Dep.Libs.ossLicensesProject)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

subprojects {
    afterEvaluate {
        project.apply("$rootDir/gradle/common.gradle")
    }
}