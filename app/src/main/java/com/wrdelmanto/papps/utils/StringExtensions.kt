package com.wrdelmanto.papps.utils

import kotlin.random.Random

/**
 * Generate a randon string.
 */
fun ClosedRange<Char>.randomString(length: Int) =
    (1..length).map {
        (Random.nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar()
    }.joinToString("")