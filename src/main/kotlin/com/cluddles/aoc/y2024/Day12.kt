package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.ArrayListGrid
import com.cluddles.aoc.util.CharGrid
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.MutableGrid
import java.util.EnumMap

/** Garden Groups */
object Day12: Solver<List<Day12.Region>, Int> {

    // Since both parts use teh same region data, we might as well generate it once up-front
    override fun prepareInput(src: SolverInput): List<Region> {
        return generateRegions(CharGrid(src.lines().toList()))
    }

    class Cell(
        val symbol: Char,
        var seen: Boolean = false,
        val edges: EnumMap<Dir4, Boolean> = EnumMap(Dir4::class.java)
    )

    val WALL = Cell('#')

    private fun convertInputToCells(input: Grid<Char>): MutableGrid<Cell> {
        return ArrayListGrid<Cell>(input.width, input.height) { i, j -> Cell(input[i, j]) }
    }

    data class Region(val symbol: Char, val perimeter: Int, val area: Int, val sides: Int) {
        constructor(symbol: Char, state: RegionWip): this(symbol, state.perimeter, state.area, state.sides)
    }

    data class RegionWip(var perimeter: Int = 0, var area: Int = 0, var sides: Int = 0)

    fun generateRegions(input: Grid<Char>): List<Region> {
        val input = convertInputToCells(input)
        var result = mutableListOf<Region>()
        for (i in 0 until input.width) {
            for (j in 0 until input.height) {
                val cell = input[i, j]
                if (!cell.seen) {
                    result += buildRegion(input, cell.symbol, i, j)
                }
            }
        }
        return result
    }

    private fun buildRegion(grid: MutableGrid<Cell>, symbol: Char, x: Int, y: Int): Region {
        val state = RegionWip(0, 0)
        connectRegion(grid, symbol, x, y, state)
        return Region(symbol, state)
    }

    // true if this cell matches symbol
    /** Attempts to expand region and update related state */
    private fun connectRegion(grid: MutableGrid<Cell>, symbol: Char, x: Int, y: Int, state: RegionWip, from: Dir4? = null): Boolean {
        val cell = if (grid.isInBounds(x, y)) grid[x, y] else WALL
        if (cell.symbol != symbol) return false

        if (!cell.seen) {
            cell.seen = true
            state.area++
            for (dir in Dir4.entries) {
                if (dir == from) continue
                if (!connectRegion(grid, symbol, x + dir.x, y + dir.y, state, dir.opposite)) {
                    state.perimeter++
                    cell.edges[dir] = true
                    // Look for matching edges in relevant adjacent cells
                    val adj = dir.rotate(1)
                    val m1 = matchingSide(grid, symbol, x - adj.x, y - adj.y, dir)
                    val m2 = matchingSide(grid, symbol, x + adj.x, y + adj.y, dir)
                    // If we found none: this is a new side!
                    if (!m1 && !m2) state.sides++
                    // If we found two: we thought there were two distinct sides, but actually it's just one
                    if (m1 && m2) state.sides--
                }
            }
        }
        return true
    }

    /** True if [grid] cell at [x],[y] has matching [symbol] and [edge] */
    private fun matchingSide(grid: Grid<Cell>, symbol: Char, x: Int, y: Int, edge: Dir4): Boolean {
        if (!grid.isInBounds(x, y)) return false
        with(grid[x, y]) { return this.symbol == symbol && this.edges[edge] == true }
    }

    override fun solvePart1(input: List<Region>): Int {
        return input.sumOf { it.perimeter * it.area }
    }

    override fun solvePart2(input: List<Region>): Int {
        return input.sumOf { it.sides * it.area }
    }

}

fun main() {
    Harness.run(Day12)
}
