package aoc.y2024

import aoc.core.Harness
import aoc.core.Solver
import aoc.core.SolverInput

/** Plutonian Pebbles */
object Day11: Solver<List<Long>, Long> {

    override fun prepareInput(src: SolverInput): List<Long> {
        return src.lines().first().split(" ").map { it.toLong() }
    }

    fun blink(stones: List<Long>): List<Long> = stones.flatMap { blinkStone(it) }

    private fun blinkStone(stone: Long) : List<Long> {
        return if (stone == 0L) {
            listOf(1)
        } else {
            val stoneStr = "$stone"
            if (stoneStr.length % 2 == 0) {
                listOf(
                    stoneStr.substring(0, stoneStr.length / 2).toLong(),
                    stoneStr.substring(stoneStr.length / 2).toLong()
                )
            } else {
                listOf(stone * 2024)
            }
        }
    }

    // Map of digit, ticks -> size
    private val cachedBlinkSizes = mutableMapOf<Pair<Long, Int>, Long>()

    private fun calculateBlinkSize(digit: Long, ticks: Int): Long {
        if (ticks == 0) return 1
        val dt = digit to ticks
        return cachedBlinkSizes[dt]
            ?: blinkStone(digit)
                .sumOf { calculateBlinkSize(it, ticks - 1) }
                .also { cachedBlinkSizes[dt] = it
        }
    }

    private fun solve(input: List<Long>, ticks: Int): Long {
        return input.sumOf { calculateBlinkSize(it, ticks) }
    }

    override fun solvePart1(input: List<Long>): Long {
        return solve(input, 25)
    }

    override fun solvePart2(input: List<Long>): Long {
        return solve(input, 75)
    }

}

fun main() {
    Harness.run(Day11)
}
