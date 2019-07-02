import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.40"
    id("org.jetbrains.dokka") version "0.9.17"
    `build-scan`
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlways()
}

version = "0.2.5-dev-2"

allprojects {
    apply(plugin = "kotlin")
    group = "org.mechdancer"
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        val kotlinVersion = getKotlinPluginVersion()

        "implementation"("org.jetbrains.kotlin", "kotlin-stdlib", kotlinVersion)

        "testImplementation"("junit:junit:4.12")
        "testImplementation"("org.jetbrains.kotlin", "kotlin-test-junit", kotlinVersion)
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

val packJars by tasks.creating(Jar::class) {
    group = JavaBasePlugin.BUILD_TASK_NAME
    description = "pack all jars"
    from(tasks.jar)
    from(dokkaJar)
}
