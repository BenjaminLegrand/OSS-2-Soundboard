package fr.legrand.oss117soundboard.presentation.ui.main.item

import android.content.Context
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.entity.MovieCharacter

class MovieCharacterViewData(private val movieCharacter: MovieCharacter) {

    var selected = false

    fun getValue() = movieCharacter.name

    fun getDisplayName(context: Context): String = when (movieCharacter) {
        MovieCharacter.HUBERT -> context.getString(R.string.character_hubert_name)
        MovieCharacter.DOLORES -> context.getString(R.string.character_dolores_name)
        MovieCharacter.ARMAND -> context.getString(R.string.character_armand_name)
        MovieCharacter.HEINRICH -> context.getString(R.string.character_heinrich_name)
        MovieCharacter.BILL -> context.getString(R.string.character_bill_name)
        MovieCharacter.VON_ZIMMEL -> context.getString(R.string.character_von_zimmel_name)
        MovieCharacter.CHINESE_BROTHERS -> context.getString(R.string.character_chinese_brothers_name)
        MovieCharacter.GERMANS -> context.getString(R.string.character_germans_name)
        MovieCharacter.CHINESE_PRINCESS -> context.getString(R.string.character_chinese_princess_name)
        MovieCharacter.CARLOTA -> context.getString(R.string.character_carlota_name)
        MovieCharacter.LEDENTU -> context.getString(R.string.character_ledentu_name)
        MovieCharacter.MAYEUX -> context.getString(R.string.character_mayeux_name)
        MovieCharacter.BEACH_WOMAN -> context.getString(R.string.character_beach_woman_name)
        MovieCharacter.BEACH_MAN -> context.getString(R.string.character_beach_man_name)
        MovieCharacter.BRESILIAN_NURSE -> context.getString(R.string.character_bresilian_nurse_name)
        MovieCharacter.ISRAELI_AGENTS -> context.getString(R.string.character_israeli_agents_name)
        MovieCharacter.VON_UMSPRUNG -> context.getString(R.string.character_von_umpsrung_name)
        MovieCharacter.JACK -> context.getString(R.string.character_jack_name)
        MovieCharacter.OTHERS -> context.getString(R.string.character_others_name)
    }
}