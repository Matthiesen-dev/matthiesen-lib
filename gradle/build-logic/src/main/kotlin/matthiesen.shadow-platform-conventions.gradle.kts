import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.architectury.plugin.TransformingTask
import net.fabricmc.loom.task.RemapJarTask
import net.fabricmc.loom.task.RemapSourcesJarTask
import org.gradle.kotlin.dsl.named

plugins {
    id("matthiesen.platform-resources-conventions")
}

pluginManager.withPlugin("com.gradleup.shadow") {
    val shadowJar = tasks.named<ShadowJar>("shadowJar") {
        archiveClassifier.set("dev-shadow")
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
    }

    tasks.named<RemapJarTask>("remapJar") {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
    }

    tasks.named<RemapSourcesJarTask>("remapSourcesJar") {
        dependsOn(shadowJar)
        inputFile.set(shadowJar.flatMap { it.archiveFile })
    }

    afterEvaluate {
        tasks.withType<TransformingTask>().configureEach {
            dependsOn(shadowJar)
            input.set(shadowJar.flatMap { it.archiveFile })
        }
    }
}

