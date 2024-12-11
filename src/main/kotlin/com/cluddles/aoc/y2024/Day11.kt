package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput

/** Plutonian Pebbles */
object Day11: Solver<List<Long>, Long> {

    override fun prepareInput(src: SolverInput): List<Long> {
        return src.lines().first().split(" ").map { it.toLong() }
    }

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

    /** Map of digit, number of blinks -> number of stones */
    private val numberOfStonesCache = mutableMapOf<Pair<Long, Int>, Long>()

    /** Determine how many stones [digit] would split into after the given number of [blinks] */
    private fun numberOfStones(digit: Long, blinks: Int): Long {
        if (blinks == 0) return 1
        val dt = digit to blinks
        return numberOfStonesCache[dt]
            ?: blinkStone(digit)
                .sumOf { numberOfStones(it, blinks - 1) }
                .also { numberOfStonesCache[dt] = it }
    }

    override fun solvePart1(input: List<Long>): Long {
        return input.sumOf { numberOfStones(it, 25) }
    }

    override fun solvePart2(input: List<Long>): Long {
        return input.sumOf { numberOfStones(it, 75) }
    }

}

fun main() {
    Harness.run(Day11)
}
