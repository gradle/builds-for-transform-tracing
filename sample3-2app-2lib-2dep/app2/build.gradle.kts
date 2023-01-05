plugins {
    application
    id("build.renamer-transform")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation(project(":lib1"))
    implementation(project(":lib2"))
}

application {
    mainClass.set("org.example.app.App")
}