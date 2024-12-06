package aoc.y2024

import aoc.util.Resource
import kotlin.math.absoluteValue

object Day01: Solver<Day01.Lists, Int> {

    data class Lists(val left: List<Int>, val right: List<Int>)

    /** Load and sort lists */
    private fun readLists(lines: Sequence<String>): Lists {
        // Split input to two lists
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()
        lines.forEach {
            val (l, r) = it.split("   ")
            left += l.toInt()
            right += r.toInt()
        }
        // Sort asc
        left.sort()
        right.sort()
        return Lists(left, right)
    }

    override fun prepareInput(path: String): Lists {
        Resource.asBufferedReader(path).useLines { return readLists(it) }
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
    Harness.run(Day01, "/2024/day01")
}
