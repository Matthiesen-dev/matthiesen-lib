plugins {
    id("dev.architectury.loom")
    id("matthiesen.api-module-conventions")
    id("matthiesen.publishing-conventions")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
}


