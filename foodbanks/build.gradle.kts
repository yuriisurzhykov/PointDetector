import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.symbol.processing)
}

ksp {
    arg(RoomSchemaArgProvider(File(projectDir, "room-schemas")))
}

android {
    namespace = "com.yuriisurzhykov.pointdetector.foodbanks"
    compileSdk = ProjectProperties.compileSdk

    defaultConfig {
        minSdk = ProjectProperties.minSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)
    implementation(libs.androidx.databinding.runtime)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.google.play.services.location)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.google.maps.utils)
    implementation(libs.google.maps.utils.ktx)
    implementation(libs.google.maps.ktx)

    implementation(libs.google.play.services.places)
    implementation(libs.google.places)

    implementation(libs.retrofit.converter.gson)
    implementation(libs.kotlin.serialization.json)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}