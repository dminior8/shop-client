package pl.dminior8.shop_client.data.local

import androidx.room.TypeConverter
import java.util.UUID

class Converters {
    @TypeConverter // konwerter typów; bo domyślnie Room obsługuje tylko konwersję typów prostych
    fun uuidToString(value: UUID?): String? = value?.toString()

    @TypeConverter
    fun stringToUUID(value: String?): UUID? = value?.let { UUID.fromString(it) }
}

