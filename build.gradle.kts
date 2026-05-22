plugins {
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.architectury.loom) apply false
    alias(libs.plugins.architectury.plugin) apply false
}


tasks.register<Copy>("copyJars") {
    group = "build"
    description = "Copies JAR files from fabric and neoforge to output directory"

    from("./api/common/build/libs/") {
        include("*.jar")
        exclude("*-dev-shadow.jar")
        exclude("*-transformProductionFabric.jar")
        exclude("*-transformProductionNeoForge.jar")
    }
    from("./api/fabric/build/libs/") {
        include("*.jar")
        exclude("*-dev-shadow.jar")
    }
    from("./api/neoforge/build/libs/") {
        include("*.jar")
        exclude("*-dev-shadow.jar")
    }

    from("./lib/common/build/libs/") {
        include("*.jar")
        exclude("*-dev-shadow.jar")
        exclude("*-transformProductionFabric.jar")
        exclude("*-transformProductionNeoForge.jar")
    }
    from("./lib/fabric/build/libs/") {
        include("*.jar")
        exclude("*-dev-shadow.jar")
    }
    from("./lib/neoforge/build/libs/") {
        include("*.jar")
        exclude("*-dev-shadow.jar")
    }
    into("./output/")

    doFirst {
        delete(fileTree("./output/") {
            include("**/*")
        })
        file("./output/").mkdirs()
    }
}
