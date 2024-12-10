package aoc.y2024

import aoc.core.Harness
import aoc.core.Solver
import aoc.core.SolverInput
import aoc.util.CharGrid
import aoc.util.Grid
import aoc.util.MutableGrid

/** Resonant Collinearity */
object Day08: Solver<Day08.Input, Int> {

    const val BLANK = '.'
    const val ANTINODE = '#'

    data class Input(val width: Int, val height: Int, val antennae: Map<Char, List<Antenna>>)
    data class Antenna(val symbol: Char, val x: Int, val y: Int)

    override fun prepareInput(src: SolverInput): Input {
        return gridToInput(CharGrid(src.lines().toList()))
    }

    /** Extract relevant input info from character [grid] */
    fun gridToInput(grid: Grid<Char>): Input {
        val antennae = mutableMapOf<Char, MutableList<Antenna>>()
        for (i in 0 until grid.width) {
            for (j in 0 until grid.height) {
                val symbol = grid[i, j]
                // Filter out antinode here too, to allow example input containing antinodes
                if (symbol != BLANK && symbol != ANTINODE) {
                    antennae.computeIfAbsent(symbol) { t -> mutableListOf() }.add(Antenna(symbol, i, j))
                }
            }
        }
        return Input(grid.width, grid.height, antennae)
    }

    private fun generateAntinodes(input: Input, useLine: Boolean): Grid<Char> {
        return CharGrid(input.width, input.height, BLANK).apply {
            for (antennae in input.antennae.values) {
                placeAntinodeList(
                    antennae,
                    this,
                    if (useLine) Day08::placeAntinodesOnLine else Day08::placeAntinodesFixed
                )
            }
        }
    }

    private fun placeAntinodeList(
        antennae: List<Antenna>,
        antinodes: MutableGrid<Char>,
        func: (Antenna, Antenna, MutableGrid<Char>) -> Any
    ) {
        for (i in antennae.indices) {
            for (j in i+1 until antennae.size) {
                func(antennae[i], antennae[j], antinodes)
            }
        }
    }

    /** Part 1: Place two antinodes at fixed position (distance to a1 == 2x distance to a2, and vice versa) */
    private fun placeAntinodesFixed(a1: Antenna, a2: Antenna, antinodes: MutableGrid<Char>) {
        val dx = a2.x - a1.x
        val dy = a2.y - a1.y
        antinodes.setIfInBounds(a1.x - dx, a1.y - dy, ANTINODE)
        antinodes.setIfInBounds(a2.x + dx, a2.y + dy, ANTINODE)
    }

    /** Part 2: Place antinodes on the line formed by a1,a2 separation */
    private fun placeAntinodesOnLine(a1: Antenna, a2: Antenna, antinodes: MutableGrid<Char>) {
        val dx = a2.x - a1.x
        val dy = a2.y - a1.y
        placeAntinodesAtIntervals(a1.x, a1.y, -dx, -dy, antinodes)
        placeAntinodesAtIntervals(a2.x, a2.y, dx, dy, antinodes)
    }

    private fun placeAntinodesAtIntervals(x: Int, y: Int, dx: Int, dy: Int, antinodes: MutableGrid<Char>) {
        var x = x
        var y = y
        do {
            antinodes[x, y] = ANTINODE
            x += dx
            y += dy
            val inBounds = antinodes.isInBounds(x, y)
        } while (inBounds)
    }

    override fun solvePart1(input: Input): Int {
        val antinodes = generateAntinodes(input, useLine = false)
        return antinodes.count { it == ANTINODE }
    }

    override fun solvePart2(input: Input): Int {
        val antinodes = generateAntinodes(input, useLine = true)
        return antinodes.count { it == ANTINODE }
    }

}

fun main() {
    Harness.run(Day08)
}
