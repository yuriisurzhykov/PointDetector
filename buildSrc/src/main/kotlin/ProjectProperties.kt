import org.gradle.api.JavaVersion

object ProjectProperties {
    val jvmSourceCompatibility = JavaVersion.VERSION_17
    val jvmTargetCompatibility = JavaVersion.VERSION_17
    const val compileSdk = 36
    const val minSdk = 26
    const val targetSdk = 36
    const val jvmTarget = "17"
}