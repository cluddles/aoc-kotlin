package aoc.y2024

import aoc.core.Harness
import aoc.core.Solver
import aoc.core.SolverInput

/** Mull It Over */
object Day03: Solver<String, Int> {

    // Processing individual lines just causes extra complication here
    override fun prepareInput(src: SolverInput): String = src.text()

    /** Sum all `mul(x,y)` operations in [chunk] */
    private fun evalSimple(chunk: String): Int {
        return Regex("mul\\((\\d+),(\\d+)\\)")
            .findAll(chunk)
            .sumOf { match ->
                val (l, r) = match.destructured
                l.toInt() * r.toInt()
            }
    }

    /** Only sum `mul(x,y)` operations in [chunk] that *aren't* preceded by `don't` */
    private fun eval(chunk: String): Int {
        val doOp = "do()"
        val dontOp = "don't()"

        // Work out all the ranges we want to calculate
        // mul() operations are initially enabled
        // Disable any time we find don't(), and re-enable when we find do()
        val doRanges = mutableListOf<IntRange>()
        var start = 0
        var end = chunk.indexOf(dontOp)
        while (end != -1) {
            doRanges += IntRange(start, end-1)
            start = chunk.indexOf(doOp, end)
            if (start != -1) {
                start += doOp.length
                end = chunk.indexOf(dontOp, start)
            } else {
                end = -1
            }
        }
        if (start != -1) {
            doRanges += IntRange(start, maxOf(end-1, chunk.length - 1))
        }
        // Slice chunk into valid ranges and eval the mul() operations for each
        return doRanges.sumOf { evalSimple(chunk.substring(it)) }
    }

    override fun solvePart1(input: String): Int {
        return evalSimple(input)
    }

    override fun solvePart2(input: String): Int {
        return eval(input)
    }

}

fun main() {
    Harness.run(Day03)
}
