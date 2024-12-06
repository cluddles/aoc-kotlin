package aoc.util

/**
 * 2d grid where each cell contains an object of type [T]
 */
interface Grid<T> : Iterable<T> {
    val width: Int
    val height: Int

    operator fun set(x: Int, y: Int, value: T)
    operator fun get(x: Int, y: Int): T

    /** Assuming elements are stored contiguously, calculate the index of given [x],[y] pos */
    fun index(x: Int, y: Int) = x + y * width
    fun isInBounds(x: Int, y: Int): Boolean = (x >= 0 && y >= 0 && x < width && y < height)
}

/**
 * Simple 2d grid of Chars
 */
class CharGrid(override val width: Int, override val height: Int, initialValue: Char = ' '): Grid<Char> {

    private val cells = CharArray(width * height) { initialValue }

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

    override operator fun set(x: Int, y: Int, value: Char) {
        cells[index(x, y)] = value
    }

    override operator fun get(x: Int, y: Int): Char = cells[index(x, y)]

    override fun iterator(): Iterator<Char> = cells.iterator()

}
