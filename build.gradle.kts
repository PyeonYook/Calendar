// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        // Firebase 관련 classpath
        classpath("com.google.gms:google-services:4.4.1")
    }

    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    // Android 및 Kotlin 플러그인 설정
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
