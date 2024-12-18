package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.IntGrid
import com.cluddles.aoc.util.MutableGrid

/** RAM Run */
object Day18: Solver<Grid<Int>, String> {

    override fun prepareInput(src: SolverInput): Grid<Int> {
        return prepareInput(src, 71, 71)
    }

    fun prepareInput(src: SolverInput, width: Int, height: Int): Grid<Int> {
        val result = IntGrid(width, height, Int.MAX_VALUE)
        for (l in src.lines().withIndex()) {
            val (x, y) = l.value.split(",").map { it.toInt() }
            result[x, y] = l.index
        }
        return result
    }

    // Quick and dirty DFS
    private fun populateCosts(input: Grid<Int>, costs: MutableGrid<Int>, x: Int, y: Int, cost: Int, bytes: Int) {
        costs[x, y] = cost
        val newCost = cost + 1
        for (d in Dir4.entries) {
            val x = x + d.x
            val y = y + d.y
            if (input.getIfInBounds(x, y) { 0 } >= bytes && newCost < costs[x, y]) {
                populateCosts(input, costs, x, y, newCost, bytes)
            }
        }
    }

    fun solvePart1(input: Grid<Int>, bytes: Int): Int {
        val costs = IntGrid(input.width, input.height, Int.MAX_VALUE)
        populateCosts(input, costs, 0, 0, 0, bytes)
        return costs[input.width-1, input.height-1]
    }

    override fun solvePart1(input: Grid<Int>): String {
        return solvePart1(input, 1024).toString()
    }

    override fun solvePart2(input: Grid<Int>): String {
        // It should be brute forceable
        // It's slow, but probably less so than me implementing a quicker algo
        // I'll make nice later
        for (i in 1..input.max()) {
            val result = solvePart1(input, i)
            if (result == Int.MAX_VALUE) {
                return with (input.iterableWithPos().first { it.data == i-1 }) { "$x,$y" }
            }
        }
        error("No solution")
    }

}

fun main() {
    Harness.run(Day18)
}
