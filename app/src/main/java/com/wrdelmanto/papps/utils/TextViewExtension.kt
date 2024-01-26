package com.wrdelmanto.papps.utils

import android.graphics.Paint.UNDERLINE_TEXT_FLAG
import android.widget.TextView

fun addUnderlineTextView(textView: TextView) {
    val paint = textView.paint

    paint.flags = paint.flags or UNDERLINE_TEXT_FLAG

    textView.invalidate()
}

fun removeUnderlineTextView(textView: TextView) {
    val paint = textView.paint

    paint.flags = paint.flags and UNDERLINE_TEXT_FLAG.inv()

    textView.invalidate()
}
