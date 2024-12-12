package com.cluddles.aoc.util

/**
 * Immutable view of 2d grid where each cell contains an object of type [T]
 */
interface Grid<T> : Iterable<T>, Debug {
    val width: Int
    val height: Int

    operator fun get(x: Int, y: Int): T

    /** Assuming elements are stored contiguously, calculate the index of given [x],[y] pos */
    fun index(x: Int, y: Int) = x + y * width
    fun isInBounds(x: Int, y: Int): Boolean = (x >= 0 && y >= 0 && x < width && y < height)

    fun mutableCopy(): MutableGrid<T>

    // I tried implementing Iterator / Sequence for Int2d positions in the grid
    // While more convenient, it was quite a bit slower than using i, j for loops? So it went in the bin
}

/**
 * 2d grid where each cell contains an object of type [T]
 */
interface MutableGrid<T> : Grid<T> {
    operator fun set(x: Int, y: Int, value: T)

    fun setIfInBounds(x: Int, y: Int, value: T) {
        if (isInBounds(x, y)) this[x, y] = value
    }
}

/**
 * Simple 2d grid of Chars
 */
class CharGrid private constructor(
    override val width: Int,
    override val height: Int,
    private val cells: CharArray
): MutableGrid<Char> {

    constructor(width: Int, height: Int, initialValue: Char = ' '):
            this(width, height, CharArray(width * height) { initialValue })

    /** Convert [lines] to CharGrid, filling in any blanks (due to short lines) with [initialValue] */
    constructor(lines: List<String>, initialValue: Char = ' '):
            this(lines.maxOf { it.length }, lines.size, initialValue) {
        for (j in 0 until height) {
            val line = lines[j]
            for (i in 0 until width) {
                this[i, j] = line[i]
            }
        }
    }

    override fun set(x: Int, y: Int, value: Char) {
        cells[index(x, y)] = value
    }

    override fun get(x: Int, y: Int): Char = cells[index(x, y)]

    override fun iterator(): Iterator<Char> = cells.iterator()

    override fun debug(): String {
        val buffer = StringBuffer()
        for (j in 0 until height) {
            buffer.append(cells, index(0, j), width).append('\n')
        }
        return buffer.toString()
    }

    override fun mutableCopy(): CharGrid = CharGrid(width, height, cells.copyOf())

}

/**
 * Grid implementation for arbitrary data type using ArrayLists
 */
class ArrayListGrid<T> private constructor(
    override val width: Int,
    override val height: Int,
    private val cells: ArrayList<T>
) : MutableGrid<T> {

    // Not the greatest, but it'll do...
    constructor(width: Int, height: Int, initialValue: (i: Int, j: Int) -> T):
            this(
                width,
                height,
                ArrayList<T>(width * height).apply {
                    for (j in 0 until height) {
                        for (i in 0 until width) {
                            this += initialValue(i, j)
                        }
                    }
                }
            )

    override fun set(x: Int, y: Int, value: T) {
        cells[index(x, y)] = value
    }

    override fun get(x: Int, y: Int): T = cells[index(x, y)]

    override fun mutableCopy(): MutableGrid<T> = ArrayListGrid(width, height, ArrayList(cells))

    override fun iterator(): Iterator<T> = cells.iterator()

    override fun debug(): String {
        TODO("Not yet implemented")
    }

}
