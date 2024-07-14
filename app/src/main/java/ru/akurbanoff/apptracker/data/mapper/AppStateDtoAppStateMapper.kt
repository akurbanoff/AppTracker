package ru.akurbanoff.apptracker.data.mapper

import ru.akurbanoff.apptracker.domain.model.AppState
import ru.akurbanoff.apptracker.storage.dto.AppStateDto
import javax.inject.Inject

class AppStateDtoAppStateMapper @Inject constructor() {
    operator fun invoke(app: AppStateDto): AppState = AppState(
        packageName = app.packageName,
        timeInApp = app.timeInApp,
    )
}
