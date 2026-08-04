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
    implementation(platform("io.ktor:ktor-bom:3.5.1"))
    implementation(platform("io.netty:netty-bom:4.2.16.Final"))
    implementation(platform("io.prometheus:prometheus-metrics-bom:1.8.0"))
    implementation(platform("tools.jackson:jackson-bom:3.2.1"))
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.22.1"))
    implementation(platform("org.eclipse.jetty:jetty-bom:12.1.11"))
    implementation(platform("org.eclipse.jetty.ee10:jetty-ee10-bom:12.1.11"))

    testImplementation(platform("org.junit:junit-bom:6.1.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(kotlin("test"))

    constraints {
        implementation("io.opentelemetry:opentelemetry-api:1.64.0")
        implementation("ch.qos.logback:logback-core:1.5.38")
        implementation("ch.qos.logback:logback-classic:1.5.38")
        implementation("com.github.jknack:handlebars:4.5.3")
        implementation("at.yawk.lz4:lz4-java:1.11.1")
        implementation("org.apache.kafka:kafka-clients:4.1.2")
        implementation("org.bouncycastle:bcprov-jdk18on:1.85")
        implementation("org.bouncycastle:bcpkix-jdk18on:1.85")
        implementation("org.bouncycastle:bcutil-jdk18on:1.85")
    }
}

tasks {
    named<Test>("test") {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
            showStackTraces = true
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
}
