// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath("app.cash.paparazzi:paparazzi-gradle-plugin:1.3.2")
    }
}
