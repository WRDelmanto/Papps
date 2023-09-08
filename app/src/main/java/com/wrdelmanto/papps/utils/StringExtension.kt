package com.wrdelmanto.papps.utils

import kotlin.random.Random

/**
 * Generate a randon string.
 *
 * @param length
 * @return Random char
 */
fun ClosedRange<Char>.randomString(length: Int) =
    (1..length).map {
        (Random.nextInt(endInclusive.code - start.code) + start.code).toChar()
    }.joinToString("")