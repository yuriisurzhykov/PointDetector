import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose.compiler)
}

android {
    namespace = "com.yuriisurzhykov.pointdetector.designsystem"
    compileSdk = ProjectProperties.compileSdk

    defaultConfig {
        minSdk = ProjectProperties.minSdk
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
    // The design system is a leaf: Compose only, no Material, no app or data modules.
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.animation)

    testImplementation(libs.junit)
}
