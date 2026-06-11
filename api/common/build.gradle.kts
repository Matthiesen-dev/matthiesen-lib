plugins {
    id("com.gradleup.shadow")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.api-shadow-platform-conventions")
    id("matthiesen.publishing-conventions")
}

architectury {
    common("neoforge", "fabric")
}

val shadowBundle: Configuration by configurations.creating

dependencies {
    // Minecraft & Mixins
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    compileOnly(libs.spongeMixin)

    // Metrics
    implementation(libs.bundles.faststats)
    shadowBundle(libs.bundles.faststats)
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        inputs.property("mod_name", project.property("api_mod_name").toString())
        filesMatching("pack.mcmeta") {
            expand(project.properties)
        }
    }

    shadowJar {
        configurations.set(listOf(shadowBundle))
        relocate("dev.faststats", "dev.matthiesen.libs.faststats")

        // FastStats uses a nested-class service file name that relocation does not rename automatically.
        filesMatching("META-INF/services/dev.faststats.SdkInfo\$UserAgentProvider") {
            name = "dev.matthiesen.libs.faststats.SdkInfo\$UserAgentProvider"
        }
    }
}