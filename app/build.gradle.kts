plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.homesecurity"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.homesecurity"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-messaging")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation ("com.polidea.rxandroidble2:rxandroidble:1.11.1")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.6")
    implementation ("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation ("com.google.dagger:hilt-android:2.40.5")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation ("com.google.android.gms:play-services-maps:19.0.0")
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$navVersion")
    val composeBom = platform("androidx.compose:compose-bom:2024.04.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation ("com.google.accompanist:accompanist-permissions:0.30.0")
    implementation ("androidx.compose.ui:ui:1.6.5")
    implementation ("androidx.compose.material:material:1.6.5")
    implementation ("androidx.compose.ui:ui-tooling:1.6.5")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.6.5")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation ("com.amplifyframework:core:2.14.11")
    implementation ("com.amazonaws:aws-android-sdk-lambda:2.22.4")
    implementation ("com.amplifyframework:aws-auth-cognito:2.14.11")
    implementation ("com.amplifyframework:core-kotlin:2.14.11")
    implementation ("com.amplifyframework.ui:authenticator:1.1.0")
    implementation ("com.amplifyframework:aws-api:2.14.11")
    implementation ("com.amplifyframework:aws-datastore:2.16.1")
    implementation("androidx.compose.material:material-icons-extended:1.6.5")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}