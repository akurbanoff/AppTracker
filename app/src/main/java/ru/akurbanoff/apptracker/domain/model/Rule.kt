package ru.akurbanoff.apptracker.domain.model

import androidx.compose.runtime.Immutable
import java.util.Calendar
import java.util.Date

@Immutable
sealed class Rule(
    open val id: Int,
    open val enabled: Boolean,
    open val packageName: String,
    open val condition: (params: Array<Any>) -> Boolean,
) {
    data class TimeLimitRule(
        override val id: Int,
        override val enabled: Boolean,
        override val packageName: String,
        val limitInSeconds: Int,
    ) : Rule(id, enabled, packageName, condition = { params ->
        val currentLimit = params[0] as Int
        currentLimit < limitInSeconds
    })

    data class HourOfTheDayRangeRule(
        override val id: Int,
        override val enabled: Boolean,
        override val packageName: String,
        val fromHour: Int,
        val fromMinute: Int,
        val toHour: Int,
        val toMinute: Int,
    ) : Rule(id, enabled, packageName, condition = { params ->

        val currentDate = Date()
        val calendar = Calendar.getInstance().apply {
            time = currentDate
        }

        var dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val startCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, fromHour)
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.MINUTE, fromMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if ((fromHour >= toHour) && (fromMinute >= toMinute)) {
            // Проверяем кейс когда юзер выбирает целый день впоть до часа и/или минуты
            ++dayOfWeek
        } else if (fromHour > toHour) {
            // Проверяем кейс когда toHour это следующий день, но время правила не целый день
            ++dayOfWeek
        }

        val endCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, toHour)
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.MINUTE, toMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        calendar.after(startCalendar) && calendar.before(endCalendar)
    })
}
