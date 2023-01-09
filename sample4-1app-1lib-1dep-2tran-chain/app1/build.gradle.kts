plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-text:1.10.0")
    implementation(project(":lib1"))
}

application {
    mainClass.set("org.example.app.App")
}

val artifactType = Attribute.of("artifactType", String::class.java)
val renamed = Attribute.of("renamed", Boolean::class.javaObjectType)
val postfixed = Attribute.of("postfixed", Boolean::class.javaObjectType)
dependencies {
    attributesSchema {
        attribute(renamed)
        attribute(postfixed)
    }
    artifactTypes.getByName("jar") {
        attributes.attribute(renamed, false)
        attributes.attribute(postfixed, false)
    }
}

configurations.all {
    afterEvaluate {
        if (isCanBeResolved) {
            attributes.attribute(postfixed, true)
        }
    }
}

dependencies {
    registerTransform(Renamer::class) {
        from.attribute(renamed, false).attribute(artifactType, "jar")
        to.attribute(renamed, true).attribute(artifactType, "jar")

        parameters {
            postfix = "-renamed"
        }
    }
}

abstract class Renamer : TransformAction<Renamer.Parameters> {
    interface Parameters : TransformParameters {
        @get:Input
        var postfix: String
    }

    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        Thread.sleep(600)
        val postfix = parameters.postfix
        val inputFile = inputArtifact.get().asFile
        val outputFile = outputs.file(inputFile.nameWithoutExtension + postfix + ".jar")
        inputFile.copyTo(outputFile)
        println("Renamed ${inputFile.name} to ${outputFile.name}")
    }
}

dependencies {
    registerTransform(Postfixer::class) {
        from.attribute(renamed, true).attribute(artifactType, "jar")
        from.attribute(postfixed, false).attribute(artifactType, "jar")
        to.attribute(postfixed, true).attribute(artifactType, "jar")

        parameters {
            postfix = "-post"
        }
    }
}

abstract class Postfixer : TransformAction<Postfixer.Parameters> {
    interface Parameters : TransformParameters {
        @get:Input
        var postfix: String
    }

    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputArtifact
    abstract val inputArtifact: Provider<FileSystemLocation>

    override fun transform(outputs: TransformOutputs) {
        Thread.sleep(400)
        val postfix = parameters.postfix
        val inputFile = inputArtifact.get().asFile
        val outputFile = outputs.file(inputFile.nameWithoutExtension + postfix + ".jar")
        inputFile.copyTo(outputFile)
        println("Postfixed ${inputFile.name} to ${outputFile.name}")
    }
}

