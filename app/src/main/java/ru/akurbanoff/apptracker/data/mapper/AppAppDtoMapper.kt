package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.storage.dto.AppDto
import javax.inject.Inject

class AppAppDtoMapper @Inject constructor() {
    operator fun invoke(app: App): AppDto = AppDto(
        id = app.id,
        packageName = app.packageName,
        enabled = app.enabled
    )
}
