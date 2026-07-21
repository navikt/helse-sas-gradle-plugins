plugins {
    `kotlin-dsl`
    `maven-publish`
}

group = "no.nav.helse.sas"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.4.0")
    implementation("com.google.cloud.tools:jib-gradle-plugin:3.5.4")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:14.2.0")
}

kotlin {
    jvmToolchain(25)
}

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/navikt/sas-gradle-plugins")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_PASSWORD")
            }
        }
    }
}
