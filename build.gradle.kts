plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "no.nav.helse.sas"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.20")
    implementation("com.google.cloud.tools:jib-gradle-plugin:3.5.4")
    implementation("com.fasterxml.jackson:jackson-bom:2.22.1") // Jib drar inn en gammel versjon ellers
    implementation("org.jlleitschuh.gradle:ktlint-gradle:14.2.0")

    constraints {
        implementation("org.apache.commons:commons-lang3:3.20.0")
    }
}

kotlin {
    jvmToolchain(25)
}

// Dependabot ser ikke inn i precompiled script plugins (src/main/kotlin/*.gradle.kts), men holder
// gradle/libs.versions.toml oppdatert. Derfor genererer vi et konstantobjekt fra versjonskatalogen
// som pluginene refererer til, slik at det finnes én kilde til sannhet for versjoner.
val libraryNotations =
    versionCatalogs.named("libs").let { catalog ->
        catalog.libraryAliases.associate { alias ->
            val dependency = catalog.findLibrary(alias).orElseThrow().get()
            val constantName = alias.replace(Regex("[^A-Za-z0-9]"), "_").uppercase()
            constantName to "${dependency.module.group}:${dependency.module.name}:${dependency.versionConstraint.requiredVersion}"
        }
    }

val generatedVersionsDir = layout.buildDirectory.dir("generated/sas-versions/kotlin")

val generateSasVersions =
    tasks.register("generateSasVersions") {
        val notations = libraryNotations
        val outputDir = generatedVersionsDir
        inputs.property("notations", notations)
        outputs.dir(outputDir)
        doLast {
            val packageDir = outputDir.get().asFile.resolve("no/nav/helse/sas")
            packageDir.mkdirs()
            packageDir.resolve("SasVersions.kt").writeText(
                buildString {
                    appendLine("// Generert fra gradle/libs.versions.toml av tasken `generateSasVersions`. Ikke rediger.")
                    appendLine("package no.nav.helse.sas")
                    appendLine()
                    appendLine("object SasVersions {")
                    notations.toSortedMap().forEach { (constantName, notation) ->
                        appendLine("""    const val $constantName = "$notation"""")
                    }
                    appendLine("}")
                },
            )
        }
    }

kotlin.sourceSets.named("main") {
    kotlin.srcDir(generateSasVersions)
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/helse-sas-gradle-plugins")
            credentials {
                username = "token"
                password = providers.environmentVariable("GITHUB_TOKEN").orNull
            }
        }
    }
}
