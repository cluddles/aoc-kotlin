package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import kotlin.time.measureTime

fun main() {
    val allSolvers2024 = listOf(
        Day01,
        Day02,
        Day03,
        Day04,
        Day05,
        Day06,
        Day07,
        Day08,
        Day09,
        Day10,
    )

    measureTime {
        for (solver in allSolvers2024) {
            Harness.run(solver)
        }
    }.also {
        println("\nTotal elapsed time for all solutions: $it")
    }
}
