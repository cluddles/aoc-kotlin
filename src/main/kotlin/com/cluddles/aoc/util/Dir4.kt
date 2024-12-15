package com.cluddles.aoc.util

import java.util.EnumMap

/**
 * Representation of cardinal (N, E, S, W) directions
 *
 * @property delta The 2d vector representation of this direction
 */
enum class Dir4(val delta: Int2d) {
    N  (Int2d( 0, -1)),
    E  (Int2d( 1,  0)),
    S  (Int2d( 0,  1)),
    W  (Int2d(-1,  0)),
    ;

    val x; get() = delta.x
    val y; get() = delta.y

    /** Rotate by the given number of 90 degree [steps] (positive for clockwise, negative for anti-clockwise) */
    fun rotate(steps: Int) : Dir4 { return move(steps) }
    val opposite: Dir4
        get() = rotate(entries.size / 2)

    fun toChar() = CHARS[ordinal]

    companion object {
        private const val CHARS = "^>v<"

        fun isValidChar(ch: Char): Boolean = CHARS.contains(ch)

        fun fromChar(ch: Char): Dir4 {
            val index = CHARS.indexOf(ch)
            require(index != -1) { "Unrecognised char '$ch'" }
            return Dir4.entries[index]
        }
    }

}
