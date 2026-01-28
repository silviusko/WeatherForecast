plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") // KSP for Room & Hilt
    // alias(libs.plugins.hilt.android) // Hilt - wait, data module usually just uses @Inject. If we want Hilt modules here, we need the plugin.
    // Ideally use javax.inject and standard Dagger modules, but Hilt is easier.
    // Often data modules are just library modules. Let's start with basic setup.
    // If we use @Module @InstallIn, we need Hilt.
    alias(libs.plugins.hilt.android)
    id("jacoco")
}

android {
    namespace = "com.ktt.weatherforecast.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        
        // We might need buildConfig for API keys if they are here
        // buildConfigField("String", "OPEN_WEATHER_API_KEY", ...) 
        // But app module has the key. We should probably pass it or inject it.
        // For now, let's enable buildConfig just in case.
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            enableUnitTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":domain"))

    // Retrofit & Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.kotlinx.coroutines.test)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    
    val debugTree = fileTree(mapOf(
        "dir" to "build/tmp/kotlin-classes/debug",
        "excludes" to listOf(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*"
        )
    ))
    
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(debugTree)
    executionData.setFrom(files("build/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"))
}
