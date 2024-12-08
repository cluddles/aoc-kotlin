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

    /**
     * Determine whether values in [stack] can evaluate to [target] using `+` or `*` operators
     * (and `||` if [allowConcat] is `true`)
     */
    fun eval(target: Long, stack: List<Long>, allowConcat: Boolean): Boolean {
        // Cannot be true if:
        // * there are no values (how?)
        // * if target is less than the top of the stack
        // * if there's only one value, and it's not equal to the target
        if (stack.size == 1) return target == stack.last()
        if (stack.isEmpty() || target < stack.last()) return false

        val mutStack = stack.toMutableList()
        val first = mutStack.removeLast()
        val second = mutStack.removeLast()
        mutStack += (first + second)
        if (eval(target, mutStack, allowConcat)) return true

        mutStack.removeLast()
        mutStack += (first * second)
        if (eval(target, mutStack, allowConcat)) return true

        if (allowConcat) {
            mutStack.removeLast()
            mutStack += (first.toString() + second.toString()).toLong()
            if (eval(target, mutStack, allowConcat)) return true
        }

        return false
    }

    override fun solvePart1(input: List<Equation>): Long {
        return input
            .filter { eval(it.answer, it.values, allowConcat = false) }
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
