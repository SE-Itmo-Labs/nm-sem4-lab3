import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("org.openjfx:javafx-controls:17.0.12")
    implementation("org.openjfx:javafx-fxml:17.0.12")

    // KOSTIL VALIDATOR
    //implementation("org.hibernate.validator:hibernate-validator:9.1.0")
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics", "javafx.base")
    // "javafx.base", "javafx.graphics", "javafx.media", "javafx.web", "javafx.swing"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

application {
    mainClass.set("com.ssnagin.collectionmanager.Client")

    applicationDefaultJvmArgs = listOf(
        "-Djdk.gtk.verbose=false",
        "-Dprism.verbose=false",
        "-Djavafx.version=2"
    )
}

tasks.jar {
    enabled = false;
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<ShadowJar>("shadowJar") {
    archiveFileName.set("collectionManager.jar")
    mergeServiceFiles()
}