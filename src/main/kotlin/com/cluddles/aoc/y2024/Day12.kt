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

    data class RegionWip(var perimeter: Int = 0, var area: Int = 0, var sides: Int = 0)

    data class Region(val symbol: Char, val perimeter: Int, val area: Int, val sides: Int) {
        constructor(symbol: Char, state: RegionWip): this(symbol, state.perimeter, state.area, state.sides)
    }

    data class Cell(
        val symbol: Char,
        var seen: Boolean = false,
        val edges: EnumMap<Dir4, Boolean> = EnumMap(Dir4::class.java)
    )

    /** Representation of cell that's out of bounds */
    val OUT_OF_BOUNDS = Cell('#')

    // Since both parts use teh same region data, we might as well generate it once up-front
    override fun prepareInput(src: SolverInput): List<Region> {
        return generateRegions(CharGrid(src.lines().toList()))
    }

    fun generateRegions(input: Grid<Char>): List<Region> {
        val input = ArrayListGrid<Cell>(input.width, input.height) { i, j -> Cell(input[i, j]) }
        var result = mutableListOf<Region>()
        input.forEachWithPos { i, j, cell ->
            if (!cell.seen) {
                result += buildRegion(input, cell, i, j)
            }
        }
        return result
    }

    private fun buildRegion(grid: MutableGrid<Cell>, cell: Cell, x: Int, y: Int): Region {
        val state = RegionWip(0, 0)
        connectRegion(grid, cell, x, y, state)
        return Region(cell.symbol, state)
    }

    /** Expand the current region and update related state */
    private fun connectRegion(grid: MutableGrid<Cell>, cell: Cell, x: Int, y: Int, state: RegionWip) {
        cell.seen = true
        state.area++
        val neighbours = Dir4.entries.map { grid.getIfInBounds(x + it.x, y + it.y) { OUT_OF_BOUNDS } }
        for (dir in Dir4.entries) {
            val other = neighbours[dir.ordinal]
            if (other.symbol != cell.symbol) {
                // This is a boundary between two regions
                state.perimeter++
                cell.edges[dir] = true
                val adj1 = neighbours[dir.rotate(1).ordinal]
                val adj2 = neighbours[dir.rotate(-1).ordinal]
                val m1 = adj1.symbol == cell.symbol && adj1.edges[dir] == true
                val m2 = adj2.symbol == cell.symbol && adj2.edges[dir] == true
                // If we found none: this is a new side!
                if (!m1 && !m2) state.sides++
                // If we found two: we thought there were two distinct sides, but actually it's just one
                if (m1 && m2) state.sides--
            } else {
                if (!other.seen) connectRegion(grid, other, x + dir.x, y + dir.y, state)
            }
        }
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
