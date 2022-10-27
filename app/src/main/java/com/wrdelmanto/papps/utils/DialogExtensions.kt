package com.wrdelmanto.papps.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wrdelmanto.papps.R

// TODO: Add Style
fun openClearSPDataDialog(context: Context) {
    MaterialAlertDialogBuilder(context)
        .setTitle(context.resources.getString(R.string.clear_all_data))
        .setMessage(context.resources.getString(R.string.clear_all_data_description))
        .setNeutralButton(context.resources.getString(R.string.cancel)) { _, _ -> } // Do nothing
        .setPositiveButton(context.resources.getString(R.string.confirm)) { _, _ -> clearSharedPreferences(context) }
    .show()
}