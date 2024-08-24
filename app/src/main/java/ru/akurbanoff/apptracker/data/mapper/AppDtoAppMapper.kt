package ru.akurbanoff.apptracker.data.mapper

import android.content.pm.PackageManager
import androidx.core.graphics.drawable.toBitmap
import ru.akurbanoff.apptracker.domain.model.App
import ru.akurbanoff.apptracker.ext.labelByPackage
import ru.akurbanoff.apptracker.storage.dto.AppDto
import javax.inject.Inject

class AppDtoAppMapper @Inject constructor() {
    operator fun invoke(app: AppDto, packageManager: PackageManager): App = with(app) {
        val appName = packageManager.labelByPackage(packageName)

        App(packageName = packageName, enabled = enabled, name = appName)
    }
}
