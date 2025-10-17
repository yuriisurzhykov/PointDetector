import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.io.IOException
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.google.firebase.appdistribution)
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
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(ProjectProperties.jvmTarget)
    }
}

dependencies {

    implementation(project(":ui-components"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)
    implementation(libs.androidx.databinding.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.play.services.location)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.android.maps.utils)
    implementation(libs.maps.utils.ktx)
    implementation(libs.maps.ktx)

    implementation(libs.play.services.places)
    implementation(libs.places)

    implementation(libs.converter.gson)
    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.database)

    // reflection-free flavor
    implementation(libs.viewbindingpropertydelegate.noreflection)
}

fun getDebugGoogleMapsApiKey(): String {
    return getFilePropertyValue("keys.properties", "API_KEY").orEmpty()
}

fun getReleaseGoogleMapsApiKey(): String {
    return System.getenv("GOOGLE_MAPS_API_KEY").orEmpty()
}

fun getReleaseVersionCode(): Int {
    try {
        val versionCode = System.getenv("VERSION_CODE")
        return versionCode.toString().ifEmpty {
            getFilePropertyValue("version.properties", "versionCode")
        }?.toIntOrNull() ?: 1
    } catch (e: Exception) {
        println("Exception: $e")
        return 1
    }
}

fun getReleaseVersionName(): String {
    val versionName = System.getenv("VERSION_NAME")
    try {
        return versionName.toString().ifEmpty {
            getFilePropertyValue("version.properties", "versionName").orEmpty()
        }
    } catch (e: Exception) {
        println("Exception: $e")
        return getFilePropertyValue("version.properties", "versionName").orEmpty()
    }
}

fun getFilePropertyValue(fileName: String, propertyName: String): String? {
    try {
        val properties = Properties()
        FileInputStream(file(fileName)).use { stream ->
            properties.load(stream)
        }
        return properties[propertyName]?.toString()
    } catch (e: IOException) {
        println("Exception: $e")
        return null
    }
}