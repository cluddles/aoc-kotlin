package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.Int2d

/** Claw Contraption */
object Day13: Solver<List<Day13.Scenario>, Long> {

    data class Scenario(val a: Int2d, val b: Int2d, val prize: Int2d)

    private val regex = Regex("[+=](\\d+).+[+=](\\d+)")

    const val PART2_OFFSET = 10000000000000L

    override fun prepareInput(src: SolverInput): List<Scenario> {
        val result = mutableListOf<Scenario>()
        val it = src.lines().iterator()
        while (it.hasNext()) {
            result.add(Scenario(parseLine(it.next()), parseLine(it.next()), parseLine(it.next())))
        }
        return result
    }

    private fun parseLine(text: String): Int2d =
        with(regex.find(text)!!) { Int2d(groupValues[1].toInt(), groupValues[2].toInt()) }

    fun score(s: Scenario, offset: Long = 0L): Long {
        val px = s.prize.x + offset
        val py = s.prize.y + offset
        val x1 = s.a.x.toLong()
        val y1 = s.a.y.toLong()
        val x2 = s.b.x.toLong()
        val y2 = s.b.y.toLong()

        // It's "just" simultaneous equations
        // (1): x1 * a + x2 * b = px
        // (2): y1 * a + y2 * b = py

        // Multiply (1) by y1:       x1 * a * y1 + x2 * b * y1 = px * y1
        // Multiply (2) by x1:       y1 * a * x1 + y2 * b * x1 = py * x1
        // (1) - (2) to eliminate a: x2 * b * y1 - y2 * b * x1 = px * y1 - py * x1
        // ...simplify:              (x2 * y1 - y2 * x1) * b = px * y1 - py * x1
        // solve for b:              b = (px * y1 - py * x1) / (x2 * y1 - y2 * x1)
        val bTop = (px * y1 - py * x1)
        val bBottom = x2 * y1 - y2 * x1
        if (bTop % bBottom != 0L) return -1
        val b = bTop / bBottom

        // using (1) now b is known: x1 * a + x2 * b = px
        // solve for a:              a = (px - x2 * b) / x1
        val aTop = px - x2 * b
        val aBottom = x1
        if (aTop % aBottom != 0L) return -1
        val a = aTop / aBottom

        return (a*3 + b)
    }

    private fun solve(input: List<Scenario>, offset: Long): Long {
        return input.map { score(it, offset) }.filter { it != -1L }.sum()
    }

    override fun solvePart1(input: List<Scenario>): Long = solve(input, 0L)

    override fun solvePart2(input: List<Scenario>): Long = solve(input, PART2_OFFSET)

}

fun main() {
    Harness.run(Day13)
}
