dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://www.jitpack.io")
        }
    }
}
include(":app")
rootProject.name = "Contact"
include(":data")
