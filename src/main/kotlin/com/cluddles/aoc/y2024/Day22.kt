package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.core.Solver

/** Monkey Market */
object Day22: Solver<List<Long>, Long> {

    // Small optimisation: Note that 16777216 is 2^24 so you can bitwise AND instead of using modulo
    fun Long.mixAndPrune(mixer: (Long) -> Long): Long =
        (mixer(this) xor this) and 16777215

    // It's slightly faster without using Sequence... but this is prettier, so I've done it this way for once
    private fun secrets(secret: Long): Sequence<Long> =
        generateSequence(secret) { s -> s.mixAndPrune { it * 64 }.mixAndPrune { it / 32 }.mixAndPrune { it * 2048 } }

    override fun prepareInput(src: SolverInput): List<Long> =
        src.lines().map { it.toLong() }.toList()

    private fun hashDifferences(a: Int, b: Int, c: Int, d: Int, e: Int) =
        (b - a) * 1000000 + (c - b) * 10000 + (d - c) * 100 + e - d

    private fun part2(secrets: Sequence<Long>, map: MutableMap<Int, Int>) {
        val seen = mutableSetOf<Int>()
        val prices = secrets.take(2001).map { (it % 10).toInt() }.toList()
        prices.forEachIndexed { i, p ->
            if (i >= 4) {
                val instruction = hashDifferences(prices[i-4], prices[i-3], prices[i-2], prices[i-1], p)
                if (seen.add(instruction)) {
                    map[instruction] = (map[instruction] ?: 0) + p
                }
            }
        }
    }

    override fun solvePart1(input: List<Long>): Long =
        input.sumOf { secrets(it).drop(2000).first() }

    override fun solvePart2(input: List<Long>): Long {
        val map = mutableMapOf<Int, Int>()
        for (i in input) { part2(secrets(i), map) }
        return map.maxOf { it.value }.toLong()
    }

}

fun main() {
    Harness.run(Day22)
}
