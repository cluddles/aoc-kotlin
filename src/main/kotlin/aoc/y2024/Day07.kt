package aoc.y2024

import aoc.core.Harness
import aoc.core.Resource
import aoc.core.Solver

/** Bridge Repair */
object Day07: Solver<List<Day07.Equation>, Long> {

    data class Equation(val answer: Long, val values: List<Long>)

    fun parseEquation(text: String): Equation {
        return with(text.split(": ")) {
            // Reverse the value list so we can use it as a stack
            return Equation(
                this[0].toLong(),
                this[1].split(" ").map { it.toLong() }.reversed())
        }
    }

    override fun prepareInput(path: String): List<Equation> {
        return Resource.asLines(path).map { parseEquation(it) }
    }

    fun concat(first: Long, second: Long): Long = (first.toString() + second.toString()).toLong()

    /**
     * Determine whether values in [stack] can evaluate to [target] using `+` or `*` operators
     * (and `||` if [allowConcat] is `true`)
     */
    fun eval(target: Long, stack: List<Long>, allowConcat: Boolean = false): Boolean {
        return eval(target, 0L, stack, allowConcat)
    }

    private fun eval(
        target: Long,
        accumulator: Long,
        stack: List<Long>,
        allowConcat: Boolean
    ): Boolean {
        // If there are no more values, we can determine the result
        if (stack.isEmpty()) return target == accumulator
        // Cannot be true if target is less than accumulator
        if (target < accumulator) return false

        // Pop off next element and eval possible operations
        val mutStack = stack.toMutableList()
        val next = mutStack.removeLast()
        return eval(target, accumulator + next, mutStack, allowConcat)
                || eval(target, accumulator * next, mutStack, allowConcat)
                || (allowConcat && eval(target, concat(accumulator, next), mutStack, true))
    }

    override fun solvePart1(input: List<Equation>): Long {
        return input
            .filter { eval(it.answer, it.values) }
            .sumOf { it.answer }
    }

    override fun solvePart2(input: List<Equation>): Long {
        return input
            .filter { eval(it.answer, it.values, allowConcat = true) }
            .sumOf { it.answer }
    }

}

fun main() {
    Harness.run(Day07, "2024/day07")
}
