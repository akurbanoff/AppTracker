plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "ru.akurbanoff.apptracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.akurbanoff.apptracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    with(libs) {
        implementation(conopas.rv)

        // Hilt
        implementation(hilt.android)
        ksp(hilt.android.compiler)

        // Room
        implementation(room.ktx)
        implementation(room.runtime)
        ksp(room.compiler)

        // Coroutines
        implementation(kotlin.android.coroutines)
        implementation(kotlin.coroutines)

        // Navigation
        implementation(navigation.compose)
        implementation(navigation.common)

        implementation(material.icons.ext)
        implementation(androidx.core.ktx)
        implementation(androidx.lifecycle.runtime.ktx)
        implementation(androidx.activity.compose)
        implementation(platform(androidx.compose.bom))
        implementation(androidx.ui)
        implementation(androidx.ui.graphics)
        implementation(androidx.ui.tooling.preview)
        implementation(androidx.material3)
        implementation(androidx.ui.text.google.fonts)
        testImplementation(junit)
        androidTestImplementation(androidx.junit)
        androidTestImplementation(androidx.espresso.core)
        androidTestImplementation(platform(androidx.compose.bom))
        androidTestImplementation(androidx.ui.test.junit4)
        debugImplementation(androidx.ui.tooling)
        debugImplementation(androidx.ui.test.manifest)
    }
}
