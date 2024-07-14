package ru.akurbanoff.apptracker.storage

import androidx.room.TypeConverter

class Converters {
    /**
     * Converts a String to a Map<String, Any>.
     *
     * @param paramsMap The String containing key-value pairs separated by '&'.
     * @return A Map<String, Any> containing the key-value pairs extracted from the input String.
     */
    @TypeConverter
    fun fromString(paramsMap: String): Map<String, Any> {
        val params: HashMap<String, String> = HashMap()
        val pairs = paramsMap.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            val key = pair.substring(0, idx)
            val value = pair.substring(idx + 1)
            params[key] = value
        }
        return params
    }

    /**
     * Converts a Map<String, Any> to a String.
     *
     * @param map The Map<String, Any> containing key-value pairs to be converted to a String.
     * @return A String representation of the key-value pairs in the input Map.
     */
    @TypeConverter
    fun fromMap(map: Map<String, Any>): String {
        val result = StringBuilder()
        for ((key, value) in map) {
            if (result.isNotEmpty()) {
                result.append("&")
            }
            result.append(key)
            result.append("=")
            result.append(value.toString())
        }
        return result.toString()
    }
}
