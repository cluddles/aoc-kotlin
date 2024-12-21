package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.CharGrid
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.Int2d

/** Race Condition */
object Day20: Solver<Grid<Char>, Int> {

    const val WALL = '#'
    const val START = 'S'

    override fun prepareInput(src: SolverInput): Grid<Char> {
        return CharGrid(src.lines().toList())
    }

    private fun nextDir(grid: Grid<Char>, current: Int2d, dir: Dir4?): Dir4? {
        for (d in Dir4.entries) {
            if (d == dir?.opposite) continue
            if (grid[current.x + d.x, current.y + d.y] == WALL) continue
            return d
        }
        return null
    }

    fun generatePath(grid: Grid<Char>): List<Int2d> {
        val start = grid.iterableWithPos().first { it.data == START }.let { Int2d(it.x, it.y) }
        val result = mutableListOf<Int2d>()
        var current = start
        var dir: Dir4? = null
        do {
            result += current
            dir = nextDir(grid, current, dir) ?: return result
            current = Int2d(current.x + dir.x, current.y + dir.y)
        } while (true)
    }

    data class Cheat(val start: Int2d, val end: Int2d)

    fun findCheats(grid: Grid<Char>, maxCheat: Int, threshold: Int): Map<Cheat, Int> {
        var cheats = mutableMapOf<Cheat, Int>()
        val path = generatePath(grid)
        for (i in 0 until path.size - threshold) {
            var j = i + threshold
            while (j < path.size) {
                val dist = path[i].manhattan(path[j])
                if (dist <= maxCheat) {
                    val saving = j - i - dist
                    if (saving >= maxOf(1, threshold)) {
                        cheats[Cheat(path[i], path[j])] = saving
                    }
                    j++
                } else {
                    // If start->end distance is more than max cheat, we know the distance can't fall back within max
                    // cheat range until a number of steps later
                    // Note: This also seems to work with just (dist - maxCheat) but that seems wrong?
                    j += (dist - maxCheat) / 2 + 1
                }
            }
        }
        return cheats
    }

    override fun solvePart1(input: Grid<Char>): Int {
        return findCheats(input, 2, 100).size
    }

    override fun solvePart2(input: Grid<Char>): Int {
        return findCheats(input, 20, 100).size
    }

}

fun main() {
    Harness.run(Day20)
}
