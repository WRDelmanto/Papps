package com.wrdelmanto.papps.utils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.wrdelmanto.papps.R

fun openClearSPDataDialog(context: Context) {
    logD { "openClearSPDataDialog" }

    MaterialAlertDialogBuilder(
        context, R.style.AlertDialogRoundedTheme
    ).setTitle(context.resources.getString(R.string.clear_all_data))
        .setMessage(context.resources.getString(R.string.clear_all_data_description))
        .setNeutralButton(context.resources.getString(R.string.cancel), null)
        .setPositiveButton(context.resources.getString(R.string.confirm)) { _, _ ->
            clearSharedPreferences(
                context
            )
            showNormalToast(context, R.string.clear_all_data_confirmation)
            logD { context.resources.getString(R.string.clear_all_data_confirmation) }
        }.show()
}