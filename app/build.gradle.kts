plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // On retire le plugin compose Multiplatform
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.todocontextuelapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.todocontextuelapp"
        minSdk = 21
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        // Assure-toi que cette version reste compatible avec la BOM
        kotlinCompilerExtensionVersion = "1.4.8"
    }
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", "true")
    }
}

dependencies {
    // -------------------------------
    // ROOM
    // -------------------------------
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    // -------------------------------
    // GÉOLOCALISATION & WORKMANAGER
    // -------------------------------
    implementation("com.google.android.gms:play-services-location:21.0.1")

    val workVersion = "2.8.1"
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    // -------------------------------
    // ANDROID CORE + MATERIAL
    // -------------------------------
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.google.android.material:material:1.8.0")

    // -------------------------------
    // TESTS UNITAIRES
    // -------------------------------
    // JUnit de base
    testImplementation("junit:junit:4.13.2")

    // Pour LiveData / Flow (InstantTaskExecutorRule)
    testImplementation("androidx.arch.core:core-testing:2.1.0")

    // Mockito (Java)
    testImplementation("org.mockito:mockito-core:4.8.0")

    // Mockito-Kotlin (coWhenever, coVerify, etc.)
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")

    // Coroutines test (runBlockingTest, etc.)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // -------------------------------
    // TESTS INSTRUMENTÉS
    // -------------------------------
    // JUnit
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // Room testing (in-memory, etc.)
    androidTestImplementation("androidx.room:room-testing:$roomVersion")

    // -------------------------------
    // COMPOSE
    // -------------------------------
    // On applique la BOM via le version catalog (libs.androidx.compose.bom)
    implementation(platform(libs.androidx.compose.bom))

    // Dépendances déclarées dans le version catalog :
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Dépendances Compose de test
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.5.3")

    // Compose Material (ancienne version) gérée par la BOM
    implementation("androidx.compose.material:material")

    // Maps Compose (catalog)
    implementation(libs.mapsCompose)

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.maps.android:maps-compose:2.11.2")

    // Gson
    implementation("com.google.code.gson:gson:2.9.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
// Et éventuellement Robolectric si tu en as besoin :
    testImplementation("org.robolectric:robolectric:4.9.2")

}

// Force jvmTarget=17 pour toutes les tâches Kotlin
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}
