import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
    id("org.jetbrains.dokka") version "0.9.17"
    `build-scan`
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlways()
}

version = "0.2.5-dev-3"

allprojects {
    apply(plugin = "kotlin")
    group = "org.mechdancer"
    repositories {
        mavenCentral()
        jcenter()
    }
    dependencies {
        implementation(kotlin("stdlib-jdk8"))

        testImplementation("junit", "junit", "+")
        testImplementation(kotlin("test-junit"))
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
