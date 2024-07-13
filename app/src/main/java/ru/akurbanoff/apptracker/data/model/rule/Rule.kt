package ru.akurbanoff.apptracker.data.model.rule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
sealed class Rule {
    @PrimaryKey
    open var id: Int = 0
    open val enabled: Boolean = false
    open val applicationId: Int = 0
}
