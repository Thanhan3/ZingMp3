plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.room")
   // id("androidx.navigation.safeargs")
 //   id("com.google.gms.google-services")
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.example.musicplayer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.musicplayer"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    room {
        schemaDirectory("$projectDir/schemas")
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
    }
}

dependencies {
    implementation(kotlin("script-runtime"))
    implementation(libs.gson)
    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.gson.converter)
    implementation(libs.media3.xoplayer)
    implementation(libs.media3.common)
    implementation(libs.media3.media.session)
    implementation(libs.media3.ui)
    implementation(libs.room.runtime)
    implementation(libs.room.paging)
    implementation(libs.room.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.espresso.core)
    ksp(libs.room.compiler)

    implementation(libs.preference)
  //  implementation(platform(libs.firebase.bom))
  //  implementation(libs.firebase.firestore)
    // for navigation
    implementation(libs.androidx.navigation.runtime)
    // for viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity)
    //
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
