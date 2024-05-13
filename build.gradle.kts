plugins {
    kotlin("multiplatform") version "1.9.24"
    id("com.vanniktech.maven.publish") version "0.25.3"
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "multimult"
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
    }
}

mavenPublishing {
    val artifactId: String by project
    coordinates(group as String, artifactId, version as String)
}
