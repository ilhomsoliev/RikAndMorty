
buildscript{
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        google()
    }
    dependencies{
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        // Hilt DI
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46.1")
        val nav_version = "2.6.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}