plugins {
    id("dev.architectury.loom")
    id("matthiesen.project-conventions")
    id("matthiesen.publishing-conventions")
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
}


