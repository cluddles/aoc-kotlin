package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.Int2d
import com.cluddles.aoc.util.IntGrid
import java.util.*

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

    // A* to find lowest cost path from start to end
    private fun lowestCost(start: Int2d, end: Int2d, grid: Grid<Int>, tick: Int): Int? {
        val g = mutableMapOf<Int2d, Int>()
        val f = mutableMapOf<Int2d, Int>()
        val open = PriorityQueue<Int2d>(Comparator.comparingInt { f[it]!! })
        g[start] = 0
        f[start] = (end - start).manhattan()
        open += start
        while (open.isNotEmpty()) {
            val current = open.remove()
            val gCurrent = g[current]!!
            if (current == end) return gCurrent
            val gNext = gCurrent + 1
            Dir4.entries
                .map { current + it.delta }
                .filter {
                    grid.getIfInBounds(it.x, it.y) { -1 } >= tick
                    && gNext < g.getOrDefault(it, Int.MAX_VALUE)
                }
                .forEach {
                    g[it] = gNext
                    f[it] = gNext + (end - it).manhattan()
                    open += it
                }
        }
        return null
    }

    fun solvePart1(input: Grid<Int>, tick: Int): Int {
        return lowestCost(Int2d(0, 0), Int2d(input.width-1, input.height-1), input, tick) ?: Int.MAX_VALUE
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
                min = current
            } else {
                max = current
            }
        }
        return with (input.iterableWithPos().first { it.data == min }) { "$x,$y" }
    }

}

fun main() {
    Harness.run(Day18)
}
