import no.nav.helse.sas.SasVersions

plugins {
    base
    id("org.jlleitschuh.gradle.ktlint")
}

ktlint {
    ignoreFailures.set(true)
    filter {
        exclude { it.file.path.contains("generated") }
    }
}

dependencies {
    constraints {
        // ktlint-pluginen drar inn logback-classic 1.3.14 -> logback-core med flere sårbarheter
        // (deserialisering av utrygg data, SSRF, EL-injection, arbitrær kodeeksekvering).
        // Løftes til nyeste patchede versjon på ktlint-konfigurasjonen.
        add("ktlint", SasVersions.LOGBACK_CLASSIC)
        add("ktlint", SasVersions.LOGBACK_CORE)
    }
}

tasks {
    // Ikke skriv ut feilformatert kode under bygging
    withType<org.jlleitschuh.gradle.ktlint.tasks.KtLintCheckTask>().configureEach {
        enabled = false
    }
    // Kjør formatering
    named<Task>("check") {
        dependsOn("ktlintFormat")
    }
}

// ktlint-pluginen legger kun git hook-tasken på rotprosjektet, og i CI vil vi ikke ha den i det hele tatt.
if (project == rootProject && providers.environmentVariable("GITHUB_ACTIONS").orNull != "true") {
    tasks.named<Task>("build") {
        dependsOn("addKtlintFormatGitPreCommitHook")
    }
}
