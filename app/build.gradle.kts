plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

baseConfig()

compose(false)

viewBinding(true)


android {
    defaultConfig {
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}

dependencies {
   // coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    androidBase()

    dataBase()

    kapt("com.google.dagger:hilt-android-compiler:2.46.1")
    kapt("androidx.hilt:hilt-compiler:1.0.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt("com.github.bumptech.glide:compiler:4.11.0")

}