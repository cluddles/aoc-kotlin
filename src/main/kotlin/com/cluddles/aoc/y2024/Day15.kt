package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.CharGrid
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.MutableGrid

/** Warehouse Woes */
object Day15: Solver<Day15.Input, Int> {

    const val BOX = 'O'
    const val WALL = '#'
    const val ROBOT = '@'
    const val BLANK = '.'

    const val BOX_L = '['
    const val BOX_R = ']'

    data class Input(val grid: Grid<Char>, val moves: List<Dir4>)

    data class Robot(var x: Int, var y: Int)

    override fun prepareInput(src: SolverInput): Input {
        val gridLines = src.lines(allowBlankLines = true)
            .takeWhile { it.isNotBlank() }
            .toList()
        val moves = src.lines(allowBlankLines = true)
            .dropWhile { it.isNotBlank() }
            .dropWhile { it.isBlank() }
            .flatMap { it.map { ch -> Dir4.fromChar(ch) } }
            .toList()
        return Input(CharGrid(gridLines), moves)
    }

    private fun findRobot(grid: Grid<Char>): Robot {
        return grid.iterableWithPos().firstOrNull { it.data == ROBOT }?.let { Robot(it.x, it.y) }
            ?: error("No robot found")
    }

    private fun applyMove(robot: Robot, dir: Dir4, grid: MutableGrid<Char>) {
        if (canMove(grid, robot.x + dir.x, robot.y + dir.y, dir)) {
            grid[robot.x, robot.y] = BLANK
            when (dir.y) {
                0 -> pushX(grid, robot.x + dir.x, robot.y, dir.x)
                else -> pushY(grid, robot.x, robot.y + dir.y, dir.y)
            }
            robot.x += dir.x
            robot.y += dir.y
            grid[robot.x, robot.y] = ROBOT
        }
    }

    private fun canMove(grid: MutableGrid<Char>, x: Int, y: Int, dir: Dir4): Boolean {
        return when(grid[x, y]) {
            WALL -> false
            BOX -> canMove(grid, x + dir.x, y + dir.y, dir)
            BOX_L -> canMove(grid, x + dir.x, y + dir.y, dir) && (dir.y == 0 || canMove(grid, x + 1, y + dir.y, dir))
            BOX_R -> canMove(grid, x + dir.x, y + dir.y, dir) && (dir.y == 0 || canMove(grid, x - 1, y + dir.y, dir))
            else -> true
        }
    }

    // X move is trivial - just shove everything along
    private fun pushX(grid: MutableGrid<Char>, x: Int, y: Int, dx: Int) {
        val orig = grid[x, y]
        when (orig) {
            WALL -> error("Cannot push")
            BOX, BOX_L, BOX_R -> {
                grid[x, y] = BLANK
                pushX(grid, x + dx, y, dx)
                grid[x + dx, y] = orig
            }
        }
    }

    // Y move is less trivial; a single box could be shoved multiple times
    // (either by a perfectly aligned pushing box, or two half-aligned boxes)
    private fun pushY(grid: MutableGrid<Char>, x: Int, y: Int, dy: Int) {
        val orig = grid[x, y]
        when (orig) {
            WALL -> { error("Cannot push") }
            BOX, BOX_L, BOX_R -> {
                grid[x, y] = BLANK
                pushY(grid, x, y + dy, dy)
                if (orig == BOX_L) {
                    grid[x + 1, y] = BLANK
                    pushY(grid, x + 1, y + dy, dy)
                    grid[x + 1, y + dy] = BOX_R
                } else if (orig == BOX_R) {
                    grid[x - 1, y] = BLANK
                    pushY(grid, x - 1, y + dy, dy)
                    grid[x - 1, y + dy] = BOX_L
                }
                grid[x, y + dy] = orig
            }
        }
    }

    private fun solve(grid: MutableGrid<Char>, moves: List<Dir4>): Int {
        val robot = findRobot(grid)
        for (m in moves) {
            applyMove(robot, m, grid)
        }
        return grid.iterableWithPos().sumOf { if (it.data == BOX || it.data == BOX_L) it.x + it.y * 100 else 0 }
    }

    override fun solvePart1(input: Input): Int {
        val grid = input.grid.mutableCopy()
        return solve(grid, input.moves)
    }

    override fun solvePart2(input: Input): Int {
        val grid = CharGrid(input.grid.width * 2, input.grid.height, BLANK)
        for (i in input.grid.iterableWithPos()) {
            val str = when(i.data) {
                WALL -> "$WALL$WALL"
                BOX -> "$BOX_L$BOX_R"
                ROBOT -> "$ROBOT$BLANK"
                else -> "$BLANK$BLANK"
            }
            grid[i.x * 2, i.y] = str[0]
            grid[i.x * 2 + 1, i.y] = str[1]
        }
        return solve(grid, input.moves)
    }

}

fun main() {
    Harness.run(Day15)
}
