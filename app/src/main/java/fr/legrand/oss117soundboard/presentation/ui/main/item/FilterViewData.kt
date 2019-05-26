package fr.legrand.oss117soundboard.presentation.ui.main.item

import android.content.Context
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.entity.FilterType

class FilterViewData(private val filterType: FilterType) {

    fun getDisplayName(context: Context): String = when (filterType) {
        FilterType.CHARACTERS -> context.getString(R.string.filter_characters)
    }

    fun getType() = filterType
}