package ru.akurbanoff.apptracker.domain.model

data class AppWithRules(
    val app: App,
    val rules: List<Rule>,
)
