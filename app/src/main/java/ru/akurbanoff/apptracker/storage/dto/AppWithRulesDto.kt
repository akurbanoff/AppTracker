package ru.akurbanoff.apptracker.storage.dto

import androidx.room.Embedded
import androidx.room.Relation
import ru.akurbanoff.apptracker.storage.dto.AppDto
import ru.akurbanoff.apptracker.storage.dto.RuleDto

data class AppWithRulesDto(
    @Embedded
    val app: AppDto,
    @Relation(
        parentColumn = "id",
        entityColumn = "applicationId"
    )
    val posts: List<RuleDto>,
)
