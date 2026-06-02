import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.fabricmc.loom.task.RemapJarTask
import org.gradle.kotlin.dsl.named

plugins {
    id("matthiesen.api-platform-resources-conventions")
}

pluginManager.withPlugin("com.gradleup.shadow") {
    val shadowJar = tasks.named<ShadowJar>("shadowJar") {
        archiveClassifier.set("dev-shadow")
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
    }

    afterEvaluate {
        tasks.withType<RemapJarTask>().configureEach {
            dependsOn(shadowJar)
            inputFile.set(shadowJar.flatMap { it.archiveFile })
        }
    }
}

