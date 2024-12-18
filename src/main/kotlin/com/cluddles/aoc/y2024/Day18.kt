package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.IntGrid
import com.cluddles.aoc.util.MutableGrid

/**
 * RAM Run
 *
 * Thought I was smart by noticing that the grid size (from `0,0` to `70,70`) is actually 71x71
 *
 * The myriad of mistakes I made after this might suggest otherwise:
 * * Reading is hard. We don't just want to add all the interruptions to the grid immediately.
 * * Reading is hard again. For a moment I thought we wanted to have the interruptions appear as we traverse.
 * * `x = x + d.y` was a painful typo that took me a while to spot
 *
 * Initial implementation as quick and easy depth-first search, which is good enough to get the answers.
 *
 * Input grid contains the "tick" that each cell becomes obstructed.
 *
 * Improvements that could be made:
 * * Use faster pathfinding algo, e.g. A*
 * * Part 2: when there's a successful path, next scan using the lowest tick that an obstruction would block it
 */
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
    private fun populateCosts(input: Grid<Int>, costs: MutableGrid<Int>, x: Int, y: Int, cost: Int, tick: Int) {
        costs[x, y] = cost
        val newCost = cost + 1
        for (d in Dir4.entries) {
            val x = x + d.x
            val y = y + d.y
            if (input.getIfInBounds(x, y) { -1 } >= tick && newCost < costs[x, y]) {
                populateCosts(input, costs, x, y, newCost, tick)
            }
        }
    }

    fun solvePart1(input: Grid<Int>, tick: Int): Int {
        val costs = IntGrid(input.width, input.height, Int.MAX_VALUE)
        populateCosts(input, costs, 0, 0, 0, tick)
        return costs[input.width-1, input.height-1]
    }

    override fun solvePart1(input: Grid<Int>): String {
        return solvePart1(input, 1024).toString()
    }

    override fun solvePart2(input: Grid<Int>): String {
        // Unless inputs are seriously janky, assume tick=0 is always good and tick=max is always bad
        var min = 0
        var max = input.filter { it != Int.MAX_VALUE }.max()
        // Quickest/dirtiest binary search I've ever written
        while (max - min > 1) {
            val current = (min + max) / 2
            val valid = (solvePart1(input, current) != Int.MAX_VALUE)
            if (valid) {
                min = maxOf(current, min + 1)
            } else {
                max = minOf(current, max - 1)
            }
        }
        return with (input.iterableWithPos().first { it.data == max-1 }) { "$x,$y" }
    }

}

fun main() {
    Harness.run(Day18)
}
