pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Add the JitPack repository
        maven {
            url = uri("https://jitpack.io" )
        }
    }
}

rootProject.name = "ChatsApp"
include(":app")
 