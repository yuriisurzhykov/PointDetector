import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "com.yuriisurzhykov.pointdetector.uicomponents"
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
    // Compose components (Pd*) are built on the theme; Compose itself comes with it as api.
    api(project(":designsystem"))

    // Previews live in src/debug only: release builds never see them.
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.core.ktx)
    implementation(libs.google.material)
    implementation(libs.kotlin.serialization.json)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}