package aoc.y2024

import aoc.core.Harness
import aoc.core.Resource
import aoc.core.Solver

/** Print Queue */
object Day05: Solver<Day05.Input, Int> {

    data class Input(val rules: Map<Int, Set<Int>>, val updates: List<Update>)
    data class Update(val pages: List<Int>)

    override fun prepareInput(path: String): Input {
        val orderingRules = mutableMapOf<Int, MutableSet<Int>>()
        val updates = mutableListOf<Update>()
        Resource.asBufferedReader(path).useLines {
            var readingRules = true
            it.forEach { l ->
                if (l.isBlank()) {
                    readingRules = false
                } else if (readingRules) {
                    val rule = l.split("|").map { it.toInt() }
                    val relevantRule = orderingRules.computeIfAbsent(rule[0]) { t -> mutableSetOf() }
                    relevantRule += rule[1]
                } else {
                    updates += parseUpdate(l)
                }
            }
        }
        return Input(orderingRules, updates)
    }

    fun parseUpdate(l: String): Update = Update(l.split(",").map { it.toInt() })

    fun isCorrectlyOrdered(rules: Map<Int, Set<Int>>, update: Update): Boolean {
        // Reject if any page is preceded by one that rules say should come after
        for (i in 1 until update.pages.size) {
            val current = update.pages[i]
            val rule = rules[current]
            if (rule == null) continue
            for (j in 0 until i) {
                if (rule.contains(update.pages[j])) return false
            }
        }
        return true
    }

    fun fixOrder(rules: Map<Int, Set<Int>>, update: Update): Update {
        // For any invalid page combination, swap them around and try again until it works
        val wip = update.pages.toMutableList()
        do {
            val swapped = fixOrderOnce(rules, wip)
        } while (swapped)
        return Update(wip)
    }

    /**
     * If any two pages in [wip] are out of order, this will swap them (in-place)
     * @return `true` if a swap occurred.
     */
    private fun fixOrderOnce(rules: Map<Int, Set<Int>>, wip: MutableList<Int>): Boolean {
        for (i in 1 until wip.size) {
            val current = wip[i]
            val rule = rules[current]
            if (rule == null) continue
            for (j in 0 until i) {
                if (rule.contains(wip[j])) {
                    wip[i] = wip[j]
                    wip[j] = current
                    return true
                }
            }
        }
        return false
    }

    /** Score for [update], which is just the middle page number */
    private fun score(update: Update) = update.pages[update.pages.size / 2]

    override fun solvePart1(input: Input): Int =
        input.updates
            .filter { isCorrectlyOrdered(input.rules, it) }
            .sumOf { score(it) }

    override fun solvePart2(input: Input): Int =
        input.updates
            .filter { !isCorrectlyOrdered(input.rules, it) }
            .map { fixOrder(input.rules, it) }
            .sumOf { score(it) }

}

fun main() {
    Harness.run(Day05, "2024/day05")
}
