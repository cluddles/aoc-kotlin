package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.CharGrid
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.MutableGrid

/** Hoof It */
object Day10: Solver<Grid<Char>, Int> {

    const val SEEN = 'X'
    const val START = '0'
    const val END = '9'

    override fun prepareInput(src: SolverInput): Grid<Char> {
        return CharGrid(src.lines().toList())
    }

    fun countPaths(input: Grid<Char>, x: Int, y: Int, uniqueEnds: Boolean): Int {
        if (input[x, y] != START) return 0
        return search(input.mutableCopy(), x, y, START, uniqueEnds)
    }

    /**
     * Recursive function to check cell at [x],[y] is [expected] and, if it is, search all neighbours for the next step.
     *
     * If [uniqueEnds] is `true`, the [grid] will be updated to prevent future calls using the same route.
     *
     * @return Number of suitable destinations reachable.
     */
    private fun search(grid: MutableGrid<Char>, x: Int, y: Int, expected: Char, uniqueEnds: Boolean): Int {
        if (!grid.isInBounds(x, y)) return 0

        val value = grid[x, y]
        if (value != expected) return 0

        if (uniqueEnds) grid[x, y] = SEEN
        if (expected == END) return 1

        val next = expected + 1
        return Dir4.entries.sumOf { search(grid, x + it.x, y + it.y, next, uniqueEnds) }
    }

    fun solve(input: Grid<Char>, uniqueEnds: Boolean): Int {
        return input.iterableWithPos().sumOf { countPaths(input, it.x, it.y, uniqueEnds) }
    }

    override fun solvePart1(input: Grid<Char>): Int {
        return solve(input, true)
    }

    override fun solvePart2(input: Grid<Char>): Int {
        return solve(input, false)
    }

}

fun main() {
    Harness.run(Day10)
}
