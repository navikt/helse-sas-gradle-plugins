import no.nav.helse.sas.SasVersions

plugins {
    id("no.nav.helse.sas.sas-module")
    id("org.jetbrains.kotlin.jvm")
}

plugins.withId("java-test-fixtures") {
    configurations.named("testFixturesImplementation") {
        extendsFrom(configurations.named("implementation").get())
    }
    configurations.named("testImplementation") {
        extendsFrom(configurations.named("testFixturesImplementation").get())
    }
}

kotlin {
    jvmToolchain(25)
}

dependencies {
    implementation(platform(SasVersions.KTOR_BOM))
    implementation(platform(SasVersions.MICROMETER_BOM))
    implementation(platform(SasVersions.NETTY_BOM))
    implementation(platform(SasVersions.PROMETHEUS_METRICS_BOM))
    implementation(platform(SasVersions.JACKSON3_BOM))
    implementation(platform(SasVersions.JACKSON2_BOM))
    implementation(platform(SasVersions.JETTY_BOM))
    implementation(platform(SasVersions.JETTY_EE10_BOM))

    testImplementation(platform(SasVersions.JUNIT_BOM))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(kotlin("test"))

    constraints {
        implementation(SasVersions.OPENTELEMETRY_API)
        implementation(SasVersions.LOGBACK_CORE)
        implementation(SasVersions.LOGBACK_CLASSIC)
        implementation(SasVersions.HANDLEBARS)
        implementation(SasVersions.LZ4_JAVA)
        implementation(SasVersions.KAFKA_CLIENTS)
        implementation(SasVersions.BCPROV)
        implementation(SasVersions.BCPKIX)
        implementation(SasVersions.BCUTIL)
    }
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
        testLogging {
            events("skipped", "failed")
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}
