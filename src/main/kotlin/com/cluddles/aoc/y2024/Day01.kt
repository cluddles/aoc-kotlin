package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.core.Solver
import kotlin.math.absoluteValue

/** Historian Hysteria */
object Day01: Solver<Day01.Lists, Int> {

    data class Lists(val left: List<Int>, val right: List<Int>)

    override fun prepareInput(src: SolverInput): Lists {
        // Split input to two lists
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        for (line in src.lines()) {
            val (l, r) = line.split("   ")
            left += l.toInt()
            right += r.toInt()
        }
        return Lists(left, right)
    }

    override fun solvePart1(lists: Lists): Int {
        return with(lists) {
            (left.sorted() zip right.sorted()).sumOf { (it.second - it.first).absoluteValue }
        }
    }

    override fun solvePart2(lists: Lists): Int {
        return with(lists) {
            left.sumOf { l -> l * right.count { r -> l == r } }
        }
    }
}

fun main() {
    Harness.run(Day01)
}
