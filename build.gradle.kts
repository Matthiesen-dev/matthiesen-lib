plugins {
    id("com.gradleup.shadow") version "9.3.1" apply false
    id("dev.architectury.loom") version("1.14-SNAPSHOT") apply false
    id("architectury-plugin") version("3.4-SNAPSHOT") apply false
}


tasks.register<Copy>("copyJars") {
    group = "build"
    description = "Copies JAR files from fabric and neoforge to output directory"

    from("./common/build/libs/") {
        include("*.jar")
        exclude("*-dev-shadow.jar")
        exclude("*-transformProductionFabric.jar")
        exclude("*-transformProductionNeoForge.jar")
    }
    from("./fabric/build/libs/") {
        include("*.jar")
        exclude("*-dev-shadow.jar")
    }
    from("./neoforge/build/libs/") {
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
