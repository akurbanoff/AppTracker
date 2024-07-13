package ru.akurbanoff.apptracker.data.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.akurbanoff.apptracker.data.model.rule.Rule

data class AppWithRules(
    @Embedded
    val app: App,
    @Relation(
        parentColumn = "id",
        entityColumn = "applicationId"
    )
    val posts: List<Rule>
)
