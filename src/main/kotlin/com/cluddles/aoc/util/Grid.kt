package com.cluddles.aoc.util

/**
 * Immutable view of 2d grid where each cell contains an object of type [T]
 */
interface Grid<T> : Iterable<T>, Debug {
    val width: Int
    val height: Int

    operator fun get(x: Int, y: Int): T
    fun getIfInBounds(x: Int, y: Int, orElse: () -> T): T = if (isInBounds(x, y)) this[x, y] else orElse()

    /** Assuming elements are stored contiguously, calculate the index of given [x],[y] pos */
    fun index(x: Int, y: Int) = x + y * width
    fun isInBounds(x: Int, y: Int): Boolean = (x >= 0 && y >= 0 && x < width && y < height)

    fun mutableCopy(): MutableGrid<T>

    /**
     * Return an [Iterable] that yields `x`, `y` and cell `data` for all cells in the grid.
     *
     * This is slightly less performant than just doing `for i, for j...`, but sometimes it's nice to write less code.
     */
    fun iterableWithPos(): CellWithPosIterable<T> = CellWithPosIterable(this)

    /**
     * Call [func] for each cell in the grid, passing through `x`, `y` and cell `data`.
     *
     * This is slightly less performant than just doing `for i, for j...`, but sometimes it's nice to write less code.
     */
    fun forEachWithPos(func: (x: Int, y: Int, data: T) -> Unit) {
        for (i in 0 until width) {
            for (j in 0 until height) {
                func(i, j, this[i, j])
            }
        }
    }
}

data class CellWithPos<T>(val x: Int, val y: Int, val data: T)

data class CellWithPosIterable<T>(val grid: Grid<T>): Iterable<CellWithPos<T>> {
    override fun iterator(): Iterator<CellWithPos<T>> = CellWithPosIterator(grid)
}

class CellWithPosIterator<T>(val grid: Grid<T>) : Iterator<CellWithPos<T>> {
    private var x = 0
    private var y = 0

    override fun hasNext(): Boolean = grid.width > x && grid.height > y

    override fun next(): CellWithPos<T> {
        if (!hasNext()) throw NoSuchElementException("Out of bounds: $x,$y")
        val result = CellWithPos(x, y, grid[x, y])
        x++
        if (x >= grid.width) { y++; x = 0 }
        return result
    }
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
        val buffer = StringBuilder()
        for (j in 0 until height) {
            buffer.append(cells, index(0, j), width).append('\n')
        }
        return buffer.toString()
    }

    override fun mutableCopy(): CharGrid = CharGrid(width, height, cells.copyOf())
}

/**
 * Simple 2d grid of Ints
 */
class IntGrid private constructor(
    override val width: Int,
    override val height: Int,
    private val cells: IntArray
): MutableGrid<Int> {

    constructor(width: Int, height: Int, initialValue: Int = 0):
            this(width, height, IntArray(width * height) { initialValue })

    override fun set(x: Int, y: Int, value: Int) {
        cells[index(x, y)] = value
    }

    override fun get(x: Int, y: Int): Int = cells[index(x, y)]

    override fun iterator(): Iterator<Int> = cells.iterator()

    override fun debug(): String {
        // TODO max length?
        val buffer = StringBuilder()
        for (j in 0 until height) {
            buffer.append(cells, index(0, j), width).append('\n')
        }
        return buffer.toString()
    }

    override fun mutableCopy(): IntGrid = IntGrid(width, height, cells.copyOf())
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
        val result = StringBuilder()
        for (j in 0 until height) {
            for (i in 0 until width) {
                val cell = this[i, j]
                if (cell is Debug) result.append(cell.debug()) else result.append("?")
            }
            result.append('\n')
        }
        return result.toString()
    }

}
