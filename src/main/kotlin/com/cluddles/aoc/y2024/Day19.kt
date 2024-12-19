package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import kotlin.collections.mutableMapOf

/** Linen Layout */
object Day19: Solver<Day19.Input, Long> {

    data class Input(val patterns: List<String>, val towels: List<String>)

    override fun prepareInput(src: SolverInput): Input {
        return Input(src.lines().first().split(", "), src.lines().drop(1).toList())
    }

    /** Can [towel] be constructed from given [patterns]. Store known results in [cache]. */
    fun isPossible(
        towel: String,
        patterns: List<String>,
        cache: MutableMap<String, Boolean> = mutableMapOf()
    ): Boolean {
        return cache[towel] ?: patterns
            .filter { p -> towel.startsWith(p) }
            .any { p -> if (p == towel) true else isPossible(towel.substring(p.length), patterns, cache) }
            .also { cache[towel] = it }
    }

    /** Count ways [towel] can be constructed from [patterns]. Store known results in [cache]. */
    fun countPossibilities(
        towel: String,
        patterns: List<String>,
        cache: MutableMap<String, Long> = mutableMapOf()
    ): Long {
        return cache[towel] ?: patterns
            .filter { p -> towel.startsWith(p) }
            .sumOf { p -> if (p == towel) 1L else countPossibilities(towel.substring(p.length), patterns, cache) }
            .also { cache[towel] = it }
    }

    override fun solvePart1(input: Input): Long {
        return input.towels.count { isPossible(it, input.patterns) }.toLong()
    }

    override fun solvePart2(input: Input): Long {
        return input.towels.sumOf { countPossibilities(it, input.patterns) }
    }

}

fun main() {
    Harness.run(Day19)
}
