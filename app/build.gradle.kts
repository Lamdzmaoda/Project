plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {

    namespace = "com.example.democode3"

    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {

        applicationId = "com.example.democode3"

        minSdk = 24

        targetSdk = 36

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_11

        targetCompatibility =
            JavaVersion.VERSION_11
    }

    buildFeatures {

        compose = true
    }
}

dependencies {

    implementation(libs.appcompat)

    implementation(libs.material)

    implementation(libs.activity)

    implementation(libs.constraintlayout)

    implementation(libs.lifecycle.runtime.ktx)

    implementation(libs.activity.compose)

    implementation(platform(libs.compose.bom))

    implementation(libs.ui)

    implementation(libs.ui.graphics)

    implementation(libs.ui.tooling.preview)

    implementation(libs.material3)

    testImplementation(libs.junit)

    androidTestImplementation(libs.ext.junit)

    androidTestImplementation(libs.espresso.core)

    androidTestImplementation(
        platform(libs.compose.bom)
    )

    androidTestImplementation(
        libs.ui.test.junit4
    )

    debugImplementation(libs.ui.tooling)

    debugImplementation(libs.ui.test.manifest)

    // =====================================
    // MATERIAL
    // =====================================

    implementation(
        "com.google.android.material:material:1.12.0"
    )

    // =====================================
    // SWIPE REFRESH
    // =====================================

    implementation(
        "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    )

    // =====================================
    // GLIDE
    // =====================================

    implementation(
        "com.github.bumptech.glide:glide:4.16.0"
    )

    annotationProcessor(
        "com.github.bumptech.glide:compiler:4.16.0"
    )

    // =====================================
    // VIEWMODEL
    // =====================================

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel:2.8.7"
    )

    implementation(
        "androidx.lifecycle:lifecycle-livedata:2.8.7"
    )
    implementation(
        "androidx.recyclerview:recyclerview:1.3.2"
    )
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}