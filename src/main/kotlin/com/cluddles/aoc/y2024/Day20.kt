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
    const val END = 'E'

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

    private fun findUniqueCheats(grid: Grid<Char>, maxCheat: Int, threshold: Int): Map<Cheat, Int> {
        var cheats = mutableMapOf<Cheat, Int>()
        val path = generatePath(grid)
        for (i in path.indices) {
            for (j in i+4 until path.size) {
                val dist = path[i].manhattan(path[j])
                if (dist <= maxCheat) {
                    val saving = j - i - dist
                    if (saving > 0 && saving >= threshold) {
                        cheats[Cheat(path[i], path[j])] = saving
                    }
                }
            }
        }
        return cheats
    }

    fun uniqueCheats(input: Grid<Char>, maxCheat: Int, threshold: Int): Map<Cheat, Int> {
        return findUniqueCheats(input, maxCheat, threshold)
    }

    override fun solvePart1(input: Grid<Char>): Int {
        return uniqueCheats(input, 2, 100).size
    }

    override fun solvePart2(input: Grid<Char>): Int {
        return uniqueCheats(input, 20, 100).size
    }

}

fun main() {
    Harness.run(Day20)
}
