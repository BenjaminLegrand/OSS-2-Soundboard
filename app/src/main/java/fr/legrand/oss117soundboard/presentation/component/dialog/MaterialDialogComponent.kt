package fr.legrand.oss117soundboard.presentation.component.dialog

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import fr.legrand.oss117soundboard.presentation.ui.base.BaseActivity
import javax.inject.Inject

class MaterialDialogComponent @Inject constructor(private val baseActivity: BaseActivity) : DialogComponent {


    private var materialDialog: MaterialDialog? = null

    override fun displaySingleChoiceDialog(
        title: Int,
        choices: List<String>,
        selected: Int,
        positiveText: Int,
        onPositiveClick: (Int) -> Unit,
        negativeText: Int,
        onNegativeClick: () -> Unit
    ) {
        materialDialog?.dismiss()
        materialDialog = MaterialDialog(baseActivity).show {
            title(title)
            listItemsSingleChoice(
                initialSelection = selected,
                items = choices,
                waitForPositiveButton = true
            ) { _, index, _ ->
                onPositiveClick(index)
            }
            positiveButton(positiveText)
            negativeButton(negativeText)
        }
    }

    override fun displayMultiChoiceDialog(
        title: Int,
        choices: List<String>,
        selected: IntArray,
        positiveText: Int,
        onPositiveClick: (IntArray) -> Unit,
        negativeText: Int,
        onNegativeClick: () -> Unit
    ) {
        materialDialog?.dismiss()
        materialDialog = MaterialDialog(baseActivity).show {
            title(title)
            listItemsMultiChoice(
                initialSelection = selected,
                items = choices,
                waitForPositiveButton = true,
                allowEmptySelection = true
            ) { _, indexes, _ ->
                onPositiveClick(indexes)
            }
            positiveButton(positiveText)
            negativeButton(negativeText)
        }
    }

    override fun dismissDialog() {
        materialDialog?.dismiss()
        materialDialog = null
    }

}