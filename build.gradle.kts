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

// Prebuild API artifacts that Loom expects to exist when configuring platform projects.
tasks.register("primeLocalApiArtifacts") {
    group = "setup"
    description = "Builds API common + platform remapped jars required for local/CI Loom metadata resolution"
    dependsOn(
        ":api-common:build",
        ":api-fabric:remapJar",
        ":api-neoforge:remapJar"
    )
}

// One command for fresh clones before IDE sync or runClient.
// NOTE: intentionally only primes the API artifacts.
// Depending on platform projects (:fabric, :neoforge) here would cause Loom to try reading the
// API fabric/neoforge jars during configuration — before any task has had a chance to produce them.
// Run this first, then `./gradlew build` (or trigger an IDE sync) as a second step.
tasks.register("bootstrapWorkspace") {
    group = "setup"
    description = "Primes local API artifacts required by Loom before IDE sync or platform builds"
    dependsOn("primeLocalApiArtifacts")
}
