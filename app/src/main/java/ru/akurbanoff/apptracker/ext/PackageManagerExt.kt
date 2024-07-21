package ru.akurbanoff.apptracker.ext

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException


fun PackageManager.labelByPackage(packageName: String): String {
    val ai = try {
        getApplicationInfo(packageName, 0)
    } catch (e: NameNotFoundException) {
        null
    }

    return if (ai != null) getApplicationLabel(ai).toString() else packageName
}

fun PackageManager.isSystemApp(packageName: String): Boolean {
    val ai = try {
        getApplicationInfo(packageName, 0)
    } catch (e: NameNotFoundException) {
        return false
    }

    return (ai.flags and ApplicationInfo.FLAG_SYSTEM) != 0
}
