import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by project
val kotest_version: String by project
val mockk_version: String by project

plugins {
    kotlin("jvm") version "1.6.21"
    `maven-publish`
    id("io.spring.dependency-management") version ("1.0.9.RELEASE")
    id("com.github.johnrengelman.shadow") version ("6.0.0")
}

group = "com.yukinissie"
version = "1.0.0"

description = "Interactive app promoting positive actions."


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.withType<ShadowJar> {
    transform(Log4j2PluginsCacheFileTransformer::class.java)
}

repositories {
    mavenCentral()
}

// If requiring AWS JDK, uncomment the dependencyManagement to use the bill of materials
//   https://aws.amazon.com/blogs/developer/managing-dependencies-with-aws-sdk-for-java-bill-of-materials-module-bom/
//dependencyManagement {
//    imports {
//        mavenBom("software.amazon.awssdk:bom:2.13.18")
//    }
//}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib:1.6.21")
    api("com.amazonaws:aws-lambda-java-core:1.2.2")
    api("com.amazonaws:aws-lambda-java-log4j2:1.5.1")
    api("org.slf4j:slf4j-simple:1.7.30")
    api("com.fasterxml.jackson.core:jackson-core:2.15.2")
    api("com.fasterxml.jackson.core:jackson-databind:2.11.0")
    api("com.fasterxml.jackson.core:jackson-annotations:2.15.2")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.0")

    testImplementation(kotlin("test-junit"))
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
}

tasks.build {
    finalizedBy(getTasksByName("shadowJar", false))
}

task<Exec>("deploy") {
    dependsOn("shadowJar")
    commandLine("serverless", "deploy")
}
