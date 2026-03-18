import org.gradle.api.JavaVersion

object ProjectProperties {
    val jvmSourceCompatibility = JavaVersion.VERSION_21
    val jvmTargetCompatibility = JavaVersion.VERSION_21
    const val compileSdk = 36
    const val minSdk = 26
    const val targetSdk = 36
    const val jvmTarget = "21"
}