package com.cluddles.aoc.util

/**
 * Representation of combined cardinal (N, E, S, W) and ordinal (NE, SE, SW, NW) directions
 *
 * @property delta The 2d vector representation of this direction
 */
enum class Dir8(val delta: Int2d) {
    N  (Int2d( 0, -1)),
    NE (Int2d( 1, -1)),
    E  (Int2d( 1,  0)),
    SE (Int2d( 1,  1)),
    S  (Int2d( 0,  1)),
    SW (Int2d(-1,  1)),
    W  (Int2d(-1,  0)),
    NW (Int2d(-1, -1)),
    ;

    val x; get() = delta.x
    val y; get() = delta.y

    /** Rotate by the given number of 45 degree [steps] (positive for clockwise, negative for anti-clockwise) */
    fun rotate(steps: Int) : Dir8 { return move(steps) }

}
