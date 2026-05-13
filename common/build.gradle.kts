plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.vanniktech.maven.publish")
}

architectury {
    common("neoforge", "fabric")
}

loom {
    silentMojangMappingsLicense()
    accessWidenerPath.set(project(":common").file("src/main/resources/matthiesen_lib.accesswidener"))
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    compileOnly("org.spongepowered:mixin:0.8.5")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit_version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${property("junit_version")}")
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(
        project.property("maven_group").toString(),
        "${rootProject.property("archives_base_name")}-${project.name}",
        "${rootProject.version}"
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
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        inputs.property("mod_name", project.property("mod_name").toString())
        filesMatching("pack.mcmeta") {
            expand(project.properties)
        }
    }

    jar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    remapJar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${rootProject.version}")
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
