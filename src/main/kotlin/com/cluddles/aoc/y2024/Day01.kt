package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.core.Solver
import kotlin.math.absoluteValue

/** Historian Hysteria */
object Day01: Solver<Day01.Lists, Int> {

    data class Lists(val left: List<Int>, val right: List<Int>)

    /** Load and sort lists */
    private fun readLists(lines: Sequence<String>): Lists {
        // Split input to two lists
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        for (line in lines) {
            val (l, r) = line.split("   ")
            left += l.toInt()
            right += r.toInt()
        }
        // Sort asc
        left.sort()
        right.sort()
        return Lists(left, right)
    }

    override fun prepareInput(src: SolverInput): Lists {
        return readLists(src.lines())
    }

    override fun solvePart1(lists: Lists): Int {
        return (lists.left zip lists.right).sumOf { (it.second - it.first).absoluteValue }
    }

    override fun solvePart2(lists: Lists): Int {
        return lists.left.sumOf { l ->
            l * lists.right.count { r -> l == r }
        }
    }
}

fun main() {
    Harness.run(Day01)
}
