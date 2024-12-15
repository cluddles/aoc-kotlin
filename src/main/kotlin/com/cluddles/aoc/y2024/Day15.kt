package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.CharGrid
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.MutableGrid
import kotlin.text.iterator

/** Warehouse Woes */
object Day15: Solver<Day15.Input, Int> {

    const val BOX = 'O'
    const val WALL = '#'
    const val ROBOT = '@'
    const val BLANK = '.'

    const val BOX_L = '['
    const val BOX_R = ']'

    data class Input(val grid: Grid<Char>, val moves: String)

    data class Robot(var x: Int, var y: Int)

    data class Box(val x: Int, val y: Int, val ch: Char)

    override fun prepareInput(src: SolverInput): Input {
        val gridLines = mutableListOf<String>()
        val it = src.lines(allowBlankLines = true).iterator()
        while (it.hasNext()) {
            val l = it.next()
            if (l.isNotBlank()) {
                gridLines.add(l)
            } else {
                break
            }
        }

        val moves = StringBuffer()
        while (it.hasNext()) {
            val l = it.next()
            moves.append(l.trimEnd())
        }

        return Input(CharGrid(gridLines), moves.toString())
    }

    private fun findRobot(grid: Grid<Char>): Robot {
        for (i in 0 until grid.width) {
            for (j in 0 until grid.height) {
                if (grid[i, j] == ROBOT) {
                    return Robot(i, j)
                }
            }
        }
        error("No robot found")
    }

    private fun applyMove(robot: Robot, move: Char, grid: MutableGrid<Char>) {
        val dir = Dir4.fromChar(move)
        if (canMove(grid, robot.x + dir.x, robot.y + dir.y, dir)) {
            grid[robot.x, robot.y] = BLANK
            if (dir.x != 0) {
                pushX(grid, robot.x + dir.x, robot.y, dir.x)
            } else {
                val adds = mutableListOf<Box>()
                pushY(grid, robot.x, robot.y + dir.y, dir.y, adds)
                for (a in adds) {
                    grid[a.x, a.y] = a.ch
                }
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
            BOX, BOX_L, BOX_R -> pushX(grid, x + dx, y, dx)
            else -> { return }
        }
        grid[x, y] = BLANK
        grid[x + dx, y] = orig
    }

    // Y move is less trivial; a single box could be shoved multiple times (either by a perfectly aligned pushing box,
    // or two half-aligned boxes)
    private fun pushY(grid: MutableGrid<Char>, x: Int, y: Int, dy: Int, adds: MutableList<Box>) {
        val orig = grid[x, y]
        when (orig) {
            WALL -> { error("Cannot push") }
            BOX, BOX_L, BOX_R -> {}
            else -> { return }
        }

        // Clear relevant cells; add boxes back into their new positions later
        grid[x, y] = BLANK
        adds.add(Box(x, y + dy, orig))
        when (orig) {
            BOX_L -> {
                grid[x + 1, y] = BLANK
                adds.add(Box(x + 1, y + dy, BOX_R))
            }
            BOX_R -> {
                grid[x - 1, y] = BLANK
                adds.add(Box(x - 1, y + dy, BOX_L))
            }
        }

        // Call through to the next box(es) in line
        pushY(grid, x, y + dy, dy, adds)
        when (orig) {
            BOX_L -> pushY(grid, x + 1, y + dy, dy, adds)
            BOX_R -> pushY(grid, x - 1, y + dy, dy, adds)
        }
    }

    private fun gps(grid: Grid<Char>): Int {
        var result = 0
        for (i in 0 until grid.width) {
            for (j in 0 until grid.height) {
                if (grid[i, j] == BOX || grid[i, j] == BOX_L) result += j * 100 + i
            }
        }
        return result
    }

    private fun solve(grid: MutableGrid<Char>, moves: String): Int {
        val robot = findRobot(grid)
        for (m in moves) {
            applyMove(robot, m, grid)
        }
        return gps(grid)
    }

    override fun solvePart1(input: Input): Int {
        val grid = input.grid.mutableCopy()
        return solve(grid, input.moves)
    }

    override fun solvePart2(input: Input): Int {
        val grid = CharGrid(input.grid.width * 2, input.grid.height, BLANK)
        for (i in 0 until input.grid.width) {
            for (j in 0 until input.grid.height) {
                val str = when(input.grid[i, j]) {
                    WALL -> "$WALL$WALL"
                    BOX -> "$BOX_L$BOX_R"
                    ROBOT -> "$ROBOT$BLANK"
                    else -> "$BLANK$BLANK"
                }
                grid[i*2, j] = str[0]
                grid[i*2 + 1, j] = str[1]
            }
        }
        return solve(grid, input.moves)
    }

}

fun main() {
    Harness.run(Day15)
}
