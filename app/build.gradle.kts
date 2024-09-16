plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlinx.serialization)
    kotlin("kapt")
}

android {
    namespace = "com.prafull.algorithms"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.prafull.algorithms"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField(
            type = "String",
            name = "API_KEY",
            value = project.findProperty("API_KEY").toString()
        )
    }
    sourceSets {
        getByName("main").java.srcDirs("build/generated/ksp/main/kotlin")
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation(libs.firebase.firestore)
    implementation(libs.firebase.appcheck.playintegrity)

    implementation(libs.androidx.room.runtime.v260)
    kapt(libs.androidx.room.compiler.v260)

    // Room Kotlin Extensions and Coroutines support
    implementation(libs.androidx.room.ktx.v260)

    // Kotlin Standard Library
    implementation(libs.kotlin.stdlib)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.viewmodel.compose)



    implementation(libs.androidx.navigation.compose)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.richeditor.compose)
    implementation(libs.kodeview)
    implementation(libs.commonmark)

    // Jsoup
    implementation(libs.jsoup)
    implementation(libs.generativeai)

    // koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.compose.shimmer)
    implementation(libs.play.services.ads)

    // coil
    implementation(libs.coil.compose)
}
kapt {
    correctErrorTypes = true
}