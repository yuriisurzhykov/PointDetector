import org.gradle.api.JavaVersion

object ProjectProperties {
    val jvmSourceCompatibility = JavaVersion.VERSION_17
    val jvmTargetCompatibility = JavaVersion.VERSION_17
    const val compileSdk = 34
    const val minSdk = 26
    const val targetSdk = 34
    const val jvmTarget = "17"
    const val composeCompiler = "1.5.4"
}