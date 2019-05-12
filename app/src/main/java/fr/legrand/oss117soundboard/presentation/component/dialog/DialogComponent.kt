package fr.legrand.oss117soundboard.presentation.component.dialog

interface DialogComponent {

    fun displaySingleChoiceDialog(title: Int, choices: List<String>, selected: Int,
                                  positiveText: Int, onPositiveClick: ((Int) -> Unit),
                                  negativeText: Int, onNegativeClick: (() -> Unit) = {})

    fun dismissDialog()
}