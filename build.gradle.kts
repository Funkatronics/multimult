plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
    jvm()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "kborsh"
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.datetime)
            }
        }
    }
}

mavenPublishing {
    val artifactId: String by project
    coordinates(group as String, artifactId, version as String)
}
