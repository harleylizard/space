import org.lwjgl.Lwjgl
import org.lwjgl.lwjgl

plugins {
    id("java")
    id("org.lwjgl.plugin") version "0.0.34"
}

group = "com.harleylizard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    lwjgl {
        implementation(Lwjgl.Module.core, Lwjgl.Module.glfw, Lwjgl.Module.opengl, Lwjgl.Module.stb)
        implementation(Lwjgl.Addons.`joml 1_10_5`)
    }
    implementation("it.unimi.dsi:fastutil:8.5.12")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}