plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "org.example.project.shared"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            // Ktor Client
            implementation("io.ktor:ktor-client-core:2.3.12")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")

            // Corrutinas y Serialización JSON
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
        }

        jvmMain.dependencies {
            implementation("io.ktor:ktor-client-cio:2.3.12")
        }

        androidMain.dependencies {
            implementation("io.ktor:ktor-client-okhttp:2.3.12")
            implementation("androidx.biometric:biometric:1.1.0")
            implementation("androidx.appcompat:appcompat:1.7.0")
            implementation("androidx.fragment:fragment-ktx:1.8.0")
            implementation("androidx.activity:activity-compose:1.9.0")
            implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
            implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.0")

            val cameraVersion = "1.3.4"
            implementation("androidx.camera:camera-core:$cameraVersion")
            implementation("androidx.camera:camera-camera2:$cameraVersion")
            implementation("androidx.camera:camera-lifecycle:$cameraVersion")
            implementation("androidx.camera:camera-view:$cameraVersion")
        }
    }
}