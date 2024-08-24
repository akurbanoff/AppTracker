package ru.akurbanoff.apptracker.domain.model

data class App(
    val packageName: String,
    val enabled: Boolean,
    val name: String? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as App

        return packageName == other.packageName
    }

    override fun hashCode(): Int {
        return packageName.hashCode()
    }
}
