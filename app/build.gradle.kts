plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.mystoryapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mystoryapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        manifestPlaceholders["GOOGLE_MAPS_API_KEY"] = findProperty("GOOGLE_MAPS_API_KEY") as String
        buildConfigField("String", "BASE_URL", "\"https://story-api.dicoding.dev/v1/\"")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.core.ktx)
    implementation(libs.androidx.datastore.preferences) // datastore
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.retrofit) //retrofit
    implementation(libs.retrofit2.converter.gson) // retrofit
    implementation(libs.logging.interceptor) // retrofit okhttp
    implementation(libs.androidx.lifecycle.runtime.ktx) // for lifecycleScope
    implementation(libs.glide)
    implementation(libs.androidx.espresso.intents) // for glide
    implementation(libs.androidx.paging.runtime.ktx) // paging 3
    implementation(libs.androidx.room.ktx) // ksp room database
    ksp(libs.room.compiler) // ksp room database

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.mockito.inline)
    testImplementation(libs.junit.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.rules)
}