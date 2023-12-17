import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "org.ks.chan.ban.shaking"
    compileSdk = 34

    defaultConfig {
        applicationId = namespace
        minSdk = 21
        targetSdk = compileSdk
        versionCode = 1
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.annotation.jvm)
    compileOnly(project(":api"))
}