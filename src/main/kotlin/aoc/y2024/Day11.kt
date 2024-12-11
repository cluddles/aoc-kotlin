package aoc.y2024

import aoc.core.Harness
import aoc.core.Solver
import aoc.core.SolverInput

/** Plutonian Pebbles */
object Day11: Solver<List<Long>, Long> {

    override fun prepareInput(src: SolverInput): List<Long> {
        return src.lines().first().split(" ").map { it.toLong() }
    }

    fun blink(stones: List<Long>): List<Long> {
        val result = mutableListOf<Long>()
        for (stone in stones) {
            if (stone == 0L) {
                result.add(1)
            } else {
                val stoneStr = "$stone"
                if (stoneStr.length % 2 == 0) {
                    result.add(stoneStr.substring(0, stoneStr.length / 2).toLong())
                    result.add(stoneStr.substring(stoneStr.length/2).toLong())
                } else {
                    result.add(stone * 2024)
                }
            }
        }
        return result
    }

    private fun calculateBlinkSize(known: Known, digit: Long, ticks: Int): Long {
        val ks = known.sizes[digit to ticks]
        if (ks != null) return ks

        val kd = known.mappings[digit]
        check(kd != null) { "Unrecognised digit: $digit" }
        val knownTicks = kd.size
        val result = if (knownTicks > ticks) {
            kd[ticks].size.toLong()
        } else {
            kd.last().sumOf { calculateBlinkSize(known, it, ticks + 1 - knownTicks) }
        }
        known.sizes[digit to ticks] = result
        return result
    }

    class Known {
        val mappings = mutableMapOf<Long, MutableList<List<Long>>>()
        val sizes = mutableMapOf<Pair<Long, Int>, Long>()
    }

    private fun solve(input: List<Long>, ticks: Int): Long {
        // Everything at age 0 is itself
        val known = Known()
        for (i in input) {
            known.mappings.computeIfAbsent(i) { mutableListOf(listOf(i)) }
        }

        // Tick our mappings the required number of times
        for (t in 0 until ticks) {
            var digits = mutableSetOf<Long>()
            for (e in known.mappings.entries) {
                // Maybe "finished" knowns should live elsewhere?
                if (e.value.size == 1 || e.value.last().any { !known.mappings.keys.contains(it) }) {
                    blink(e.value.last()).apply {
                        e.value.add(this)
                        digits.addAll(this)
                    }
                }
            }

            for (i in digits) {
                known.mappings.computeIfAbsent(i) { mutableListOf(listOf(i)) }
            }
        }

        return input.sumOf { calculateBlinkSize(known, it, ticks) }
    }

    override fun solvePart1(input: List<Long>): Long {
        return solve(input, 25)
//        var result: List<Long> = input
//        repeat(25) { result = blink(result) }
//        return result.size.toLong()
    }

    override fun solvePart2(input: List<Long>): Long {
        return solve(input, 75)
    }

}

fun main() {
    Harness.run(Day11)
}
