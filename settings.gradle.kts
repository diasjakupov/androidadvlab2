pluginManagement {
    val localProperties = java.util.Properties().apply {
        val localPropertiesFile = file("local.properties")
        if (localPropertiesFile.exists()) {
            load(java.io.FileInputStream(localPropertiesFile))
        }
    }

    // Access properties from local.properties
    val githubUser = localProperties.getProperty("USERNAME")
    val githubToken = localProperties.getProperty("TOKEN")

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/diasjakupov/websocket-echo-chat")

            credentials {
                username = githubUser
                password = githubToken
            }
        }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    val localProperties = java.util.Properties().apply {
        val localPropertiesFile = file("local.properties")
        if (localPropertiesFile.exists()) {
            load(java.io.FileInputStream(localPropertiesFile))
        }
    }

    // Access properties from local.properties
    val githubUser = localProperties.getProperty("USERNAME")
    val githubToken = localProperties.getProperty("TOKEN")


    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/diasjakupov/websocket-echo-chat")
            credentials {
                username = githubUser
                password = githubToken
            }
        }
        google()
        mavenCentral()
    }
}

rootProject.name = "androidadvlab2"
include(":app")
include(":websocket-chat")
