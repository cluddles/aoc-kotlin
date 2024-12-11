package com.cluddles.aoc.util

import kotlin.math.absoluteValue

/**
 * Representation of an immutable 2D integer vector
 *
 * @property x X component
 * @property y Y component
 */
data class Int2d(val x: Int, val y: Int) {

    operator fun plus(o: Int2d) = Int2d(x + o.x, y + o.y)
    operator fun times(o: Int2d) = Int2d(x * o.x, y * o.x)
    operator fun times(o: Int) = Int2d(x * o, y * o)

    fun manhattan(): Int = (x + y).absoluteValue

}
