package com.wrdelmanto.papps.utils

/**
 * Round to 2 decimals.
 *
 * @param number
 */
fun roundTo2Decimals(number: Double): String = "%.2f".format(number)

/**
 * Check if the input is numeric (Int, Float, Double, etc.)
 *
 * @param value
 * @return True if is numeric
 */
fun isNumeric(value: String): Boolean = value.toDoubleOrNull() != null