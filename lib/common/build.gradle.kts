plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("matthiesen.minecraft-module-conventions")
    id("matthiesen.publishing-conventions")
}

architectury {
    common("neoforge", "fabric")
}

dependencies {
    // Minecraft & Mixins
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    compileOnly(libs.spongeMixin)

    // Depend on matthiesen-lib-api mod
    implementation(project(":api-common", configuration = "namedElements"))
}


tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        inputs.property("mod_name", project.property("mod_name").toString())
        filesMatching("pack.mcmeta") {
            expand(project.properties)
        }
    }
}
