import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
}

val resolvedModVersion = providers.environmentVariable("RELEASE_VERSION")
    .orElse(providers.gradleProperty("mod_version"))
    .orNull
    ?: error("Set RELEASE_VERSION or mod_version (e.g. in gradle.properties / -Pmod_version=...)")

group = property("maven_group").toString()
version = resolvedModVersion

project.repositories {
    mavenCentral()
    maven("https://artefacts.cobblemon.com/releases/")
    maven("https://repo.spongepowered.org/repository/maven-public")
    maven("https://api.modrinth.com/maven")
    maven("https://maven.tysontheember.dev/")
    maven("https://repo.faststats.dev/releases")
}

dependencies.add("testImplementation", "org.junit.jupiter:junit-jupiter-api:${property("junit_version")}")
dependencies.add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:${property("junit_version")}")

configure<JavaPluginExtension> {
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}


