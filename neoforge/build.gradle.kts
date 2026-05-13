plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.vanniktech.maven.publish")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.neoforged.net/releases/")
}

val shadowBundle: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    minecraft("net.minecraft:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    neoForge("net.neoforged:neoforge:${property("neoforge_version")}")

    implementation(project(":common", configuration = "namedElements"))
    "developmentNeoForge"(project(":common", configuration = "namedElements")) {
        isTransitive = false
    }
    shadowBundle(project(":common", configuration = "transformProductionNeoForge"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit_version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junit_version")}")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(
        project.group.toString(),
        "${rootProject.property("archives_base_name")}-${project.name}",
        project.version.toString()
    )

    pom {
        name.set(project.property("mod_name").toString())
        description.set(project.property("mod_description").toString())
        inceptionYear.set("2020")
        url.set(project.property("github_url").toString())
        licenses {
            license {
                name.set(project.property("mod_license").toString())
                url.set(project.property("mod_license_url").toString())
                distribution.set(project.property("mod_license_url").toString())
            }
        }
        developers {
            developer {
                id.set(project.property("mod_author_id").toString())
                name.set(project.property("mod_author").toString())
                url.set(project.property("mod_author_url").toString())
            }
        }
        scm {
            url.set(project.property("github_url").toString())
            connection.set("scm:git:git://${project.property("git_url").toString()}")
            developerConnection.set("scm:git:ssh://git@${project.property("git_url").toString()}")
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    processResources {
        inputs.property("mod_id", project.property("mod_id").toString())
        inputs.property("version", project.version)
        inputs.property("mod_name", project.property("mod_name").toString())
        inputs.property("mod_description", project.property("mod_description").toString())
        inputs.property("mod_license", project.property("mod_license").toString())
        inputs.property("mod_author", project.property("mod_author").toString())
        inputs.property("github_url", project.property("github_url").toString())

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(project.properties)
        }
    }

    jar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    shadowJar {
        exclude("fabric.mod.json")
        archiveClassifier.set("dev-shadow")
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        configurations = listOf(shadowBundle)
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.version}")
    }

    remapSourcesJar {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
