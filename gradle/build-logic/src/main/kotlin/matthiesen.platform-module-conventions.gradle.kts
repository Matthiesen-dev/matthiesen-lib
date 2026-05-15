import net.fabricmc.loom.api.LoomGradleExtensionAPI
import org.gradle.kotlin.dsl.configure

plugins {
    id("matthiesen.minecraft-module-conventions")
}

pluginManager.withPlugin("dev.architectury.loom") {
    configure<LoomGradleExtensionAPI> {
        enableTransitiveAccessWideners.set(true)
    }
}

