plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}
android {
    namespace = "ie.djroche.datalogviewer"
    compileSdk = 34

    defaultConfig {
        applicationId = "ie.djroche.datalogviewer"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.github.ajalt:timberkt:1.5.1")

    //barcode
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    // google JSON To Object converter
    implementation("com.google.code.gson:gson:2.9.1")
    // volly FTTP Lib for requests
    implementation("com.android.volley:volley:1.2.1")
    // settings
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.test:monitor:1.6.1")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    // fragments
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    // Required -- JUnit 4 framework
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}