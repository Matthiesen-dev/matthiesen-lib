plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.api-module-conventions")
    id("matthiesen.publishing-conventions")
}

architectury {
    common("neoforge", "fabric")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    compileOnly(libs.spongeMixin)
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        inputs.property("mod_name", project.property("api_mod_name").toString())
        filesMatching("pack.mcmeta") {
            expand(project.properties)
        }
    }
}