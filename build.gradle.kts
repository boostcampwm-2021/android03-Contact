buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://www.jitpack.io")
        }
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

allprojects {
    repositories {
        maven {
            setUrl("https://www.jitpack.io")
        }
    }
}

subprojects {
    afterEvaluate {
        project.apply("$rootDir/gradle/common.gradle")
    }
}