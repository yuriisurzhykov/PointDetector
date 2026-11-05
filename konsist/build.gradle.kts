/*
 * Architecture tests for the whole project: design-system layering, no hardcoded tokens,
 * no nullable or business types in UI contracts, one type per file, file size.
 * Rules are plain functions over source files (see src/main), the tests apply them.
 */
plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    testImplementation(libs.konsist)
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()

    // The rules read the sources of every module, so a change there must rerun them;
    // without declared inputs Gradle reports the task up-to-date and misses a violation.
    inputs.files(
        rootProject.fileTree(rootDir) {
            include("*/src/**/*.kt")
            exclude("konsist/**", "**/build/**")
        },
    ).withPropertyName("scannedSources").withPathSensitivity(PathSensitivity.RELATIVE)
}
