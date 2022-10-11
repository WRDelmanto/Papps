package com.wrdelmanto.papps.utils

import android.content.res.Resources
import kotlin.math.absoluteValue

/**
 * @return True if font is large or largest.
 */
fun isFontOrDisplayLarge() = isFontSizeLarge()

/**
 * @return Font size.
 */
fun getFontSize(): String {
    val fontSize = Resources.getSystem().configuration.fontScale.absoluteValue
    return when (fontSize.toString()) {
        "0.85" -> "Small"
        "1.0" -> "Normal"
        "1.15" -> "Large"
        "1.3" -> "Largest"
        else -> "Unable to get font size"
    }
}

/**
 * @return True if font is large or largest.
 */
fun isFontSizeLarge(): Boolean = getFontSize() == "Large" || getFontSize() == "Largest"