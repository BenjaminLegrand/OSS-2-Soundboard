package fr.legrand.oss117soundboard.data.entity.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.legrand.oss117soundboard.data.entity.MovieCharacter


class CharacterListTypeConverter {

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<MovieCharacter>>() {}.type

    @TypeConverter
    fun toCharacterList(value: String): List<MovieCharacter> =
        gson.fromJson(value, typeToken)

    @TypeConverter
    fun toValue(characters: List<MovieCharacter>): String = gson.toJson(characters)

}