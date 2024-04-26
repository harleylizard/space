import org.lwjgl.Lwjgl
import org.lwjgl.lwjgl

plugins {
    id("java")
    id("org.lwjgl.plugin") version "0.0.34"
    id("io.github.sgtsilvio.gradle.proguard") version "0.6.0"
}

group = "com.harleylizard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    lwjgl {
        implementation(Lwjgl.Module.core, Lwjgl.Module.glfw, Lwjgl.Module.opengl, Lwjgl.Module.stb)
        implementation(Lwjgl.Addons.`joml 1_10_5`)
    }
    implementation("it.unimi.dsi:fastutil:8.5.12")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.guava:guava:33.1.0-jre")

    implementation("com.guardsquare:proguard-gradle:7.3.2")

    testImplementation("com.github.Querz:NBT:6.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

val proguardJar by tasks.registering(proguard.taskClass) {
    addInput {
        classpath.from(tasks.jar)
    }
    addOutput {
        archiveFile.set(base.libsDirectory.file("${project.name}-${project.version}-obfuscated.jar"))
    }
    jdkModules.add("java.base")
    mappingFile.set(base.libsDirectory.file("${project.name}-${project.version}-mapping.txt"))

    rules.addAll(
        "-dontwarn !com.harleylizard.**",
        "-keep class com.harleylizard.space.Main { public static void main(java.lang.String[]); }",
        "-keepattributes Signature,InnerClasses,*Annotation*",
    )
}