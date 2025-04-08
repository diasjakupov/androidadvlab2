import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

// Place the version of your library here
val versionName: String = "1.0.4"

// Add the name of your library here
val libArtifactId: String = "websocket-echo-chat"

// Add the group ID of your library here
val libGroupId: String = "kz.diasjakupov"

// Prepare URL of maven package.
val gitHubUrl: String = "https://maven.pkg.github.com/diasjakupov/websocket-echo-chat"


android {
    namespace = "kz.diasjakupov.websocket_chat"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures{
        viewBinding = true
    }
}

val localProps = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

private fun getLocalProperty(key: String) = localProps.getProperty(key)

publishing {
    publications {
        register<MavenPublication>("release")  {
            groupId = libGroupId
            artifactId = libArtifactId
            version = versionName
            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri(gitHubUrl)
            credentials {
                username = getLocalProperty("USERNAME")
                password = getLocalProperty("TOKEN")
            }
        }
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.websockets)
    implementation(libs.ktor.client.logging)
}