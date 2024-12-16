package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.CharGrid
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.Int2d
import com.cluddles.aoc.util.IntGrid
import com.cluddles.aoc.util.MutableGrid

/** Reindeer Maze */
object Day16: Solver<Grid<Char>, Int> {

    const val WALL = '#'
    const val START = 'S'
    const val END = 'E'

    override fun prepareInput(src: SolverInput): Grid<Char> {
        return CharGrid(src.lines().toList())
    }

    /** Populate all cell [costs] with a simple flood fill */
    private fun populateCosts(costs: MutableGrid<Int>, input: Grid<Char>, x: Int, y: Int, dir: Dir4, cost: Int) {
        val bestCost = costs[x, y]
        if (bestCost >= 0 && bestCost < cost) return
        if (input[x, y] == WALL) return
        costs[x, y] = cost
        populateCosts(costs, input, x + dir.x, y + dir.y, dir, cost + 1)
        with (dir.rotate(1))  { populateCosts(costs, input, x + this.x, y + this.y, this, cost + 1001) }
        with (dir.rotate(-1)) { populateCosts(costs, input, x + this.x, y + this.y, this, cost + 1001) }
    }

    private fun findStartAndEnd(input: Grid<Char>): Pair<Int2d, Int2d> {
        val start = input.iterableWithPos().firstOrNull { it.data == START }?.let { Int2d(it.x, it.y) }
            ?: error("No start")
        val end = input.iterableWithPos().firstOrNull { it.data == END }?.let { Int2d(it.x, it.y) }
            ?: error("No end")
        return start to end
    }

    override fun solvePart1(input: Grid<Char>): Int {
        val (start, end) = findStartAndEnd(input)
        val grid = IntGrid(input.width, input.height, -1)
        populateCosts(grid, input, start.x, start.y, Dir4.E, 0)
        return grid[end.x, end.y]
    }

    /** Given generated [costs], we can determine all [result] nodes that sit on one of the best paths */
    private fun backtrack(costs: Grid<Int>, x: Int, y: Int, result: MutableSet<Int2d>, lastCost: Int, lastDir: Dir4?) {
        val here = Int2d(x, y)
        if (!result.contains(here)) {
            result += here
            val cost = costs[x, y]
            var dirs = if (lastDir == null) Dir4.entries else listOf(lastDir, lastDir.rotate(1), lastDir.rotate(-1))
            for (d in dirs) {
                val nextCost = costs[x + d.x, y + d.y]
                if (nextCost == -1) continue
                if (cost - nextCost == 1 || cost - nextCost == 1001 || lastCost - nextCost == 2) {
                    backtrack(costs, x + d.x, y + d.y, result, cost, d)
                }
            }
        }
    }

    override fun solvePart2(input: Grid<Char>): Int {
        val (start, end) = findStartAndEnd(input)
        val grid = IntGrid(input.width, input.height, -1)
        populateCosts(grid, input, start.x, start.y, Dir4.E, 0)
        val result = mutableSetOf<Int2d>()
        backtrack(grid, end.x, end.y, result, -1, null)
        return result.size
    }

}

fun main() {
    Harness.run(Day16)
}
