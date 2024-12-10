package aoc.y2024

import aoc.core.Harness
import aoc.core.Solver
import aoc.core.SolverInput

/** Bridge Repair */
object Day07: Solver<Sequence<Day07.Equation>, Long> {

    data class Equation(val answer: Long, val values: List<Long>)

    /** Operation that can be applied to two values */
    fun interface Op {
        fun apply(first: Long, second: Long): Long
    }

    val opAdd = Op { first, second -> first + second }
    val opMul = Op { first, second -> first * second }
    val opConcat = Op { first, second -> "$first$second".toLong() }

    val opsPart1 = listOf(opAdd, opMul)
    val opsPart2 = listOf(opAdd, opMul, opConcat)

    fun parseEquation(text: String): Equation {
        return with(text.split(": ")) {
            // Reverse the value list so we can use it as a more efficient stack
            // (pop from end without having to shuffle contents down)
            return Equation(
                this[0].toLong(),
                this[1].split(" ").map { it.toLong() })
        }
    }

    override fun prepareInput(src: SolverInput): Sequence<Equation> {
        return src.lines().map { parseEquation(it) }
    }

    /**
     * Determine whether values in [stack] can evaluate to [target] using specified [ops]
     *
     * @param target Value we want
     * @param stack Available values to combine
     * @param pos Current position in stack
     * @param ops Available operations
     * @param accumulator Current evaluation total so far
     */
    fun eval(target: Long, stack: List<Long>, ops: List<Op>, pos: Int = 0, accumulator: Long = 0L): Boolean {
        // If there are no more values, we can determine the result
        if (pos == stack.size) return target == accumulator
        // Cannot be true if target is less than accumulator
        if (target < accumulator) return false

        // Pop off next element and eval possible operations
        val next = stack[pos]
        return ops.any { eval(target, stack, ops, pos + 1, it.apply(accumulator, next)) }
    }

    override fun solvePart1(input: Sequence<Equation>): Long {
        return input
            .filter { eval(it.answer, it.values, opsPart1) }
            .sumOf { it.answer }
    }

    override fun solvePart2(input: Sequence<Equation>): Long {
        return input
            .filter { eval(it.answer, it.values, opsPart2) }
            .sumOf { it.answer }
    }

}

fun main() {
    Harness.run(Day07)
}
