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
    const val BEST = 'O'

    override fun prepareInput(src: SolverInput): Grid<Char> {
        return CharGrid(src.lines().toList())
    }

    private fun floodFill(grid: MutableGrid<Int>, input: Grid<Char>, x: Int, y: Int, dir: Dir4, cost: Int) {
        val score = grid[x, y]
        if (score >= 0 && score < cost) return
        if (input[x, y] == WALL) return
        grid[x, y] = cost
        floodFill(grid, input, x + dir.x, y + dir.y, dir, cost + 1)
        with (dir.rotate(1))  { floodFill(grid, input, x + this.x, y + this.y, this, cost + 1001) }
        with (dir.rotate(-1)) { floodFill(grid, input, x + this.x, y + this.y, this, cost + 1001) }
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
        floodFill(grid, input, start.x, start.y, Dir4.E, 0)
        return grid[end.x, end.y]
    }

    data class Explore(val x: Int, val y: Int, val dir: Dir4)

    private fun bfs(input: Grid<Char>, start: Int2d, startDir: Dir4, end: Int2d): Int {
        val toExplore = mutableMapOf(Explore(start.x, start.y, startDir) to 0)
        val explored = mutableMapOf<Explore, Int>()

        while (toExplore.isNotEmpty()) {
            val entry = toExplore.minBy { it.value }
            val ex = entry.key
            val cost = entry.value
            toExplore.remove(ex)
            if (explored.getOrDefault(ex, Int.MAX_VALUE) < cost) continue
            explored[ex] = cost
            // This assumes the goal is only reachable from a single best direction
            if (ex.x == end.x && ex.y == end.y) {
                val grid = input.mutableCopy()
                backtrack(grid, explored, ex)
                return grid.count { it == BEST }
            }

            if (input[ex.x + ex.dir.x, ex.y + ex.dir.y] != WALL) {
                toExplore[Explore(ex.x + ex.dir.x, ex.y + ex.dir.y, ex.dir)] = cost + 1
            }
            toExplore[Explore(ex.x, ex.y, ex.dir.rotate(1))] = cost + 1000
            toExplore[Explore(ex.x, ex.y, ex.dir.rotate(-1))] = cost + 1000
        }
        error("No solution")
    }

    private fun backtrack(grid: MutableGrid<Char>, explored: Map<Explore, Int>, ex: Explore) {
        grid[ex.x, ex.y] = BEST
        val cost = explored[ex]!!
        val cell = grid[ex.x - ex.dir.x, ex.y - ex.dir.y]
        if (cell != WALL && cell != BEST) {
            val back = Explore(ex.x - ex.dir.x, ex.y - ex.dir.y, ex.dir)
            if (explored.getOrDefault(back, Int.MAX_VALUE) == cost - 1) {
                backtrack(grid, explored, back)
            }
        }

        val right = Explore(ex.x, ex.y, ex.dir.rotate(1))
        val rightCost = explored.getOrDefault(right, Int.MAX_VALUE)
        if (rightCost == cost - 1000) backtrack(grid, explored, right)

        val left = Explore(ex.x, ex.y, ex.dir.rotate(-1))
        val leftCost = explored.getOrDefault(left, Int.MAX_VALUE)
        if (leftCost == cost - 1000) backtrack(grid, explored, left)
    }

    override fun solvePart2(input: Grid<Char>): Int {
        val (start, end) = findStartAndEnd(input)
        return bfs(input, start, Dir4.E, end)
    }

}

fun main() {
    Harness.run(Day16)
}
