package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.domain.model.AppState
import ru.akurbanoff.apptracker.storage.dto.AppStateDto
import javax.inject.Inject

class AppStateAppStateDtoMapper @Inject constructor() {
    operator fun invoke(app: AppState): AppStateDto = AppStateDto(
        packageName = app.packageName,
        timeInApp = app.timeInApp,
    )
}
