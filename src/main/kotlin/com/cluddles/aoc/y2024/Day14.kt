package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.CharGrid
import com.cluddles.aoc.util.Int2d
import java.io.File

/** Restroom Redoubt */
object Day14: Solver<List<Day14.Robot>, Int> {

    data class Robot(val pos: Int2d, val vel: Int2d)

    override fun prepareInput(src: SolverInput): List<Robot> {
        return src.lines().map { parseRobot(it) }.toList()
    }

    private fun parseRobot(line: String): Robot {
        with(line.split(" ")) {
            return Robot(parseVec(this[0]), parseVec(this[1]))
        }
    }

    // p=1,-2 or similar
    private fun parseVec(text: String): Int2d {
        return with(text.split("=")[1].split(",").map { it.toInt() }) { Int2d(this[0], this[1]) }
    }

    private fun calculateRobotPosition(robot: Robot, steps: Int, worldSize: Int2d): Robot {
        return Robot(
            Int2d(
                Math.floorMod(robot.pos.x + robot.vel.x * steps, worldSize.x),
                Math.floorMod(robot.pos.y + robot.vel.y * steps, worldSize.y)
            ),
            robot.vel
        )
    }

    private fun safetyFactor(robots: List<Robot>, worldSize: Int2d): Int {
        val quadCounts = IntArray(4)
        val mx = worldSize.x/2
        val my = worldSize.y/2
        for (r in robots) {
            if (r.pos.x == mx || r.pos.y == my) continue
            val quadIndex = r.pos.x / (mx+1) + (r.pos.y / (my+1)) * 2
            quadCounts[quadIndex]++
        }
        return quadCounts.fold(1) { a, b -> a * b }
    }

    fun solvePart1(input: List<Robot>, worldSize: Int2d): Int {
        return safetyFactor(input.map { calculateRobotPosition(it, 100, worldSize) }, worldSize)
    }

    val worldSize = Int2d(101, 103)

    override fun solvePart1(input: List<Robot>): Int {
        return solvePart1(input, worldSize)
    }

    override fun solvePart2(input: List<Robot>): Int {
        var robots = input
        val grid = CharGrid(worldSize.x, worldSize.y, ' ')
        for (i in 1..100000) {
            robots = robots.map { calculateRobotPosition(it, 1, worldSize) }
            for (r in robots) grid[r.pos.x, r.pos.y] = '#'
            if (grid.debug().contains("##########")) return i
            for (r in robots) grid[r.pos.x, r.pos.y] = ' '
        }
        error("Not found")
    }

}

fun main() {
    Harness.run(Day14)
}
