plugins {
    application
    id("build.renamer-transform")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation(project(":lib1"))
}

application {
    mainClass.set("org.example.app.App")
}
