plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.kaupark"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kaupark"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation("androidx.fragment:fragment:1.8.4")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.filament.android)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation (libs.androidx.viewpager2)
    implementation("com.naver.maps:map-sdk:3.19.1")
    implementation(platform("com.google.firebase:firebase-bom:33.5.1")) // Firebase BOM 추가
    implementation("com.google.firebase:firebase-firestore-ktx") // Firebase Firestore 추가
    implementation("com.google.firebase:firebase-auth-ktx") // Firebase Auth 추가
    implementation ("com.google.firebase:firebase-database-ktx:20.0.4")
    implementation ("android.arch.lifecycle:extensions:1.1.1")
    implementation ("androidx.fragment:fragment-ktx:1.5.7'")
    implementation ("androidx.activity:activity-ktx:1.7.1")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")


}

