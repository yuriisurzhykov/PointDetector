import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("kotlinx-serialization")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    id("com.google.devtools.ksp")
}

kapt {
    correctErrorTypes = true
}

ksp {
    arg(RoomSchemaArgProvider(File(projectDir, "schemas")))
}

android {
    compileSdk = ProjectProperties.compileSdk
    namespace = "com.yuriisurzhykov.pointdetector"

    defaultConfig {
        applicationId = "com.yuriisurzhykov.pointdetector"
        minSdk = ProjectProperties.minSdk
        targetSdk = ProjectProperties.targetSdk
        versionCode = getReleaseVersionCode()
        versionName = getReleaseVersionName()

        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
    lint {
        abortOnError = false
    }
    signingConfigs {
        create("release") {
            val properties = Properties()
            FileInputStream(file("signing.properties")).use { stream ->
                properties.load(stream)
            }
            storeFile = file(properties.getProperty("storeFile"))
            storePassword = properties.getProperty("storePassword").toString()
            keyAlias = properties.getProperty("keyAlias").toString()
            keyPassword = properties.getProperty("keyPassword").toString()
        }
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            buildConfigField("String", "GOOGLE_MAPS_API_KEY", getDebugGoogleMapsApiKey())
        }
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
                "serializer-rules.pro"
            )
            buildConfigField("String", "GOOGLE_MAPS_API_KEY", getReleaseGoogleMapsApiKey())
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = ProjectProperties.jvmSourceCompatibility
        targetCompatibility = ProjectProperties.jvmTargetCompatibility
    }
    kotlinOptions {
        jvmTarget = ProjectProperties.jvmTarget
    }
}

dependencies {

    implementation(project(":ui-components"))

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("androidx.databinding:databinding-runtime:8.5.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    val hilt_version = "2.51.1"
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-compiler:$hilt_version")

    implementation("com.google.android.gms:play-services-location:21.3.0")

    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.8.1")

    implementation("com.google.maps.android:android-maps-utils:2.4.0")
    implementation("com.google.maps.android:maps-utils-ktx:3.4.0")
    implementation("com.google.maps.android:maps-ktx:3.2.1")

    implementation("com.google.android.gms:play-services-places:17.1.0")
    implementation("com.google.android.libraries.places:places:3.5.0")

    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    // reflection-free flavor
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.6")
}

fun getDebugGoogleMapsApiKey(): String {
    try {
        val properties = Properties()
        FileInputStream(file("keys.properties")).use { stream ->
            properties.load(stream)
        }
        return properties.getProperty("API_KEY") ?: emptyString()
    } catch (_: Exception) {
        return emptyString()
    }
}

fun getReleaseGoogleMapsApiKey(): String {
    return System.getenv("GOOGLE_MAPS_API_KEY") ?: emptyString()
}

fun emptyString(): String {
    return "\"\""
}

fun getReleaseVersionCode(): Int {
    val versionCode = System.getenv("VERSION_CODE")
    try {
        return versionCode.toString().toInt()
    } catch (e: Exception) {
        println("Exception: ${e}")
        return 3
    }
}

fun getReleaseVersionName(): String {
    var versionName = System.getenv("VERSION_NAME")
    try {
        if (versionName.isEmpty()) {
            versionName = "1.3"
        }
        return versionName.toString() + "." + getReleaseVersionCode().toString()
    } catch (e: Exception) {
        println("Exception: ${e}")
        return "1.3"
    }
}