package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import kotlin.math.absoluteValue
import kotlin.math.sign

/** Red-Nosed Reports */
object Day02: Solver<List<Day02.Report>, Int> {

    data class Report(val values: List<Int>)

    override fun prepareInput(src: SolverInput): List<Report> {
        return src.lines()
            .map { l -> Report(l.split(" ").map { v -> v.toInt() }) }
            .toList()
    }

    private fun isSafe(report: Report): Boolean {
        val sign = (report.values[1] - report.values[0]).sign
        for (i in 1 until report.values.size) {
            val diff = report.values[i] - report.values[i-1]
            if (diff.sign != sign || diff.absoluteValue == 0 || diff.absoluteValue > 3) return false
        }
        return true
    }

    private fun isSafeWithinTolerance(report: Report): Boolean {
        if (isSafe(report)) return true
        // Brute force: remove each element in turn and see if any of these permutations are safe
        for (i in 0 until report.values.size) {
            val newReport = Report(report.values.toMutableList().apply { removeAt(i) })
            if (isSafe(newReport)) return true
        }
        return false
    }

    override fun solvePart1(input: List<Report>): Int {
        return input.count { l -> isSafe(l) }
    }

    override fun solvePart2(input: List<Report>): Int {
        return input.count { l -> isSafeWithinTolerance(l) }
    }

}

fun main() {
    Harness.run(Day02)
}
