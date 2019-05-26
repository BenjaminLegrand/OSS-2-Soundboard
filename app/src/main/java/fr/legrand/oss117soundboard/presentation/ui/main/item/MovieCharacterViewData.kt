package fr.legrand.oss117soundboard.presentation.ui.main.item

import android.content.Context
import fr.legrand.oss117soundboard.data.entity.MovieCharacter

class MovieCharacterViewData(private val movieCharacter: MovieCharacter) {

    var selected = false

    fun getValue() = movieCharacter.name

    fun getDisplayName(context: Context): String = when (movieCharacter) {
        //TODO put into resources
        MovieCharacter.HUBERT -> "Hubert"
        MovieCharacter.DOLORES -> "Dolores"
        MovieCharacter.ARMAND -> "Armand"
        MovieCharacter.HEINRICH -> "Heinrich"
        MovieCharacter.BILL -> "Bill"
        MovieCharacter.VON_ZIMMEL -> "Von Zimmel"
        MovieCharacter.CHINESE_BROTHERS -> "Les Chinois"
        MovieCharacter.GERMANS -> "Les Allemands"
        MovieCharacter.CHINESE_PRINCESS -> "Princesse Chinoise"
        MovieCharacter.CARLOTA -> "Carlota"
        MovieCharacter.LEDENTU -> "Ledentu"
        MovieCharacter.MAYEUX -> "Mayeux"
        MovieCharacter.BEACH_WOMAN -> "Femme plage"
        MovieCharacter.BEACH_MAN -> "Homme plage"
        MovieCharacter.BRESILIAN_NURSE -> "Infirmière"
        MovieCharacter.ISRAELI_AGENTS -> "Agents israéliens"
        MovieCharacter.BEACH -> "Plage"
    }
}