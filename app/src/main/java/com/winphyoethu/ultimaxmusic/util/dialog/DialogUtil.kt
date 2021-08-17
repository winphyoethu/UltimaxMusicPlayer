package com.winphyoethu.ultimaxmusic.util.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.lang.Exception
import javax.inject.Inject

class DialogUtil @Inject constructor() {

    fun showMessageDialog(
        context: Context,
        positiveClicked: () -> Unit,
        negativeClicked: () -> Unit
    ) {

        val builder = AlertDialog.Builder(context)
            .setTitle("Warning")
            .setMessage("Are you sure to remove this song from playlist?")
            .setPositiveButton("Delete") { dialog, _ ->
                dialog.dismiss()
                positiveClicked()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                negativeClicked()
            }
        val dialog = builder.create()

        try {
            dialog.show()
        } catch (e: Exception) {

        }

    }

}