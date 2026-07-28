plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "no.nav.helse.sas"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.10")
    implementation("com.google.cloud.tools:jib-gradle-plugin:3.5.4")
    implementation("com.fasterxml.jackson:jackson-bom:2.22.1") // Jib drar inn en gammel versjon ellers
    implementation("org.jlleitschuh.gradle:ktlint-gradle:14.2.0")
}

kotlin {
    jvmToolchain(25)
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
