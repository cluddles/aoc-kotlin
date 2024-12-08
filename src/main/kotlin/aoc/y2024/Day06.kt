package aoc.y2024

import aoc.core.Harness
import aoc.core.Resource
import aoc.core.Solver
import aoc.util.CharGrid
import aoc.util.Dir4
import aoc.util.Grid
import aoc.util.MutableGrid

/** Guard Gallivant */
object Day06: Solver<Grid<Char>, Int> {

    const val WALL = '#'
    val GUARD_DIRS = charArrayOf('^', '>', 'v', '<')

    data class Guard(var x: Int, var y: Int, var facing: Dir4 = Dir4.N)

    override fun prepareInput(path: String): Grid<Char> {
        return CharGrid(Resource.asLines(path), '.')
    }

    private fun findGuard(grid: Grid<Char>): Guard {
        for (i in 0 until grid.width) {
            for (j in 0 until grid.height) {
                if (grid[i, j] == GUARD_DIRS[0]) {
                    return Guard(i, j)
                }
            }
        }
        throw IllegalArgumentException("No guard found")
    }

    private fun visit(grid: MutableGrid<Char>, guard: Guard) {
        grid[guard.x, guard.y] = GUARD_DIRS[guard.facing.ordinal]
    }

    private fun moveGuardOnce(grid: MutableGrid<Char>, guard: Guard) {
        // Check if we need to turn due to wall
        val targetX = guard.x + guard.facing.x
        val targetY = guard.y + guard.facing.y
        if (grid.isInBounds(targetX, targetY) && grid[targetX, targetY] == WALL) {
            // Turn and end step
            guard.facing = guard.facing.rotate(1)
            return
        }
        // Mark cell as visited, move to destination
        visit(grid, guard)
        guard.x = targetX
        guard.y = targetY
    }

    private fun generateGuardPath(grid: Grid<Char>): Grid<Char> {
        val mutGrid = grid.mutableCopy()
        val guard = findGuard(mutGrid)
        do {
            moveGuardOnce(mutGrid, guard)
            val inBounds = mutGrid.isInBounds(guard.x, guard.y)
        } while (inBounds)
        return mutGrid
    }

    override fun solvePart1(input: Grid<Char>): Int {
        return generateGuardPath(input)
            .count { GUARD_DIRS.contains(it) }
    }

    private fun causesLoop(grid: Grid<Char>, x: Int, y: Int): Boolean {
        val mutGrid = grid.mutableCopy()
        mutGrid[x, y] = WALL
        val guard = findGuard(mutGrid)
        do {
            moveGuardOnce(mutGrid, guard)
            val inBounds = mutGrid.isInBounds(guard.x, guard.y)
            if (inBounds && mutGrid[guard.x, guard.y] == GUARD_DIRS[guard.facing.ordinal]) return true
        } while (inBounds)
        return false
    }

    override fun solvePart2(input: Grid<Char>): Int {
        val guardStart = findGuard(input)
        val grid = generateGuardPath(input)
        // For every visited position that isn't the guard start pos, insert a wall and see if it causes a loop
        var looping = 0
        for (i in 0 until grid.width) {
            for (j in 0 until grid.height) {
                if (i == guardStart.x && j == guardStart.y) continue
                if (!GUARD_DIRS.contains(grid[i, j])) continue
                if (causesLoop(input, i, j)) looping++
            }
        }
        return looping
    }

}

fun main() {
    Harness.run(Day06, "2024/day06")
}
