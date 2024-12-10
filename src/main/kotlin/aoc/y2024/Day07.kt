package aoc.y2024

import aoc.core.Harness
import aoc.core.Solver
import aoc.core.SolverInput

/** Bridge Repair */
object Day07: Solver<Sequence<Day07.Equation>, Long> {

    data class Equation(val answer: Long, val values: List<Long>)

     /** Operation that can be applied to two values */
     interface Op {
         /** Combine [a] and [b] using this operation */
        fun combine(a: Long, b: Long): Long

        /**
         * Apply the inverse of the operation, such that:
         * ```
         * combine(a, b) = total
         * inverse(b, total) = a
         * ```
         *
         * Returns `-1` if the operation is not valid.
         */
        fun inverse(b: Long, total: Long): Long
    }

    object OpAdd : Op {
        override fun combine(a: Long, b: Long) = a + b
        override fun inverse(b: Long, total: Long) = total - b
    }

    object OpMul : Op {
        override fun combine(a: Long, b: Long) = a * b
        override fun inverse(b: Long, total: Long) = if (total % b != 0L) -1 else total / b
    }

    object OpConcat : Op {
        override fun combine(a: Long, b: Long) = "$a$b".toLong()
        override fun inverse(b: Long, total: Long): Long {
            val firstStr = "$b"
            val secondStr = "$total"
            return if (secondStr.endsWith(firstStr)) {
                secondStr.substring(0, secondStr.length - firstStr.length).toLong()
            } else {
                -1L
            }
        }
    }

    val opsPart1 = listOf(OpAdd, OpMul)
    val opsPart2 = listOf(OpAdd, OpMul, OpConcat)

    fun parseEquation(text: String): Equation {
        return with(text.split(": ")) {
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
     * @param ops Available operations
     * @param pos Current position in stack
     * @param accumulator Current evaluation total so far
     */
    fun eval(target: Long, stack: List<Long>, ops: List<Op>, pos: Int = 0, accumulator: Long = 0L): Boolean {
        // If there are no more values, we can determine the result
        if (pos == stack.size) return target == accumulator
        // Cannot be true if target is less than accumulator
        if (target < accumulator) return false

        // Pop off next element and eval possible operations
        val next = stack[pos]
        return ops.any { eval(target, stack, ops, pos + 1, it.combine(accumulator, next)) }
    }

    /**
     * Flipped version of [eval], where we start with the target value and apply the inverse of each operation, aiming
     * for a final result of zero.
     *
     * This runs ~95% quicker, as you can quickly eliminate invalid solutions (e.g. if `accumulator/next` isn't a whole
     * number then the multiply operator is not possible)
     *
     * @param accumulator Current value
     * @param stack Available values to combine
     * @param ops Available operations
     * @param pos Current position in stack
     */
    fun evalInverse(accumulator: Long, stack: List<Long>, ops: List<Op>, pos: Int = stack.size - 1): Boolean {
        return when {
            pos < 0 -> accumulator == 0L
            accumulator < 0L -> false
            else -> {
                ops.any { evalInverse(it.inverse(stack[pos], accumulator), stack, ops, pos - 1) }
            }
        }
    }

    override fun solvePart1(input: Sequence<Equation>): Long {
        return input
            .filter { evalInverse(it.answer, it.values, opsPart1) }
            .sumOf { it.answer }
    }

    override fun solvePart2(input: Sequence<Equation>): Long {
        return input
            .filter { evalInverse(it.answer, it.values, opsPart2) }
            .sumOf { it.answer }
    }

}

fun main() {
    Harness.run(Day07)
}
