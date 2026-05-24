import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.external.javadoc.StandardJavadocDocletOptions
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.named

plugins {
    id("matthiesen.project-conventions")
}

pluginManager.withPlugin("dev.architectury.loom") {
    configure<LoomGradleExtensionAPI> {
        silentMojangMappingsLicense()
    }

    tasks.named<Jar>("jar") {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveClassifier.set("dev-slim")
    }

    tasks.named<RemapJarTask>("remapJar") {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set(project.version.toString())
    }

    tasks.named<RemapSourcesJarTask>("remapSourcesJar") {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("sources")
    }
}


