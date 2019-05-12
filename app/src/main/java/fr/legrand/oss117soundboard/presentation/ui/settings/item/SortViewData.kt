package fr.legrand.oss117soundboard.presentation.ui.settings.item

import android.content.Context
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.values.SortType

class SortViewData(context: Context, private val sort: SortType) {

    private val sortValues = mapOf(
        SortType.ALPHABETICAL_SORT to context.getString(R.string.alphabetical_order),
        SortType.MOVIE_SORT to context.getString(R.string.movie_order),
        SortType.RANDOM_SORT to context.getString(R.string.random_order)
    )
    private val sortKeys = sortValues.keys.toList()

    val sortString = sortValues.values.toList()

    fun getSortText(context: Context): String =
        sortValues.getOrElse(sort, { context.getString(R.string.alphabetical_order) })

    fun getSelectedIndex(): Int = sortKeys.indexOf(sort)

    fun getValue(index: Int): SortType = sortKeys[index]

}