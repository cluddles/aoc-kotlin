package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.core.Solver

/** LAN Party */
object Day23: Solver<Map<String, Set<String>>, String> {

    /** Map of each computer to its direct connections */
    override fun prepareInput(src: SolverInput): Map<String, Set<String>> {
        val connections = mutableMapOf<String, MutableSet<String>>()
        for (l in src.lines()) {
            val (pc1, pc2) = l.split("-")
            connections.getOrPut(pc1) { mutableSetOf() } += pc2
            connections.getOrPut(pc2) { mutableSetOf() } += pc1
        }
        return connections
    }

    override fun solvePart1(input: Map<String, Set<String>>): String {
        // Every 3-element containing a computer starting with "t"
        val valid = mutableSetOf<MutableSet<String>>()
        for ((pc1, conns1) in input) {
            if (!pc1.startsWith("t")) continue
            for (pc2 in conns1) {
                for (pc3 in input[pc2]!!) {
                    if (conns1.contains(pc3)) {
                        valid += mutableSetOf(pc1, pc2, pc3)
                    }
                }
            }
        }
        return valid.size.toString()
    }

    override fun solvePart2(input: Map<String, Set<String>>): String {
        // Largest set where each computer is connected to each other computer
        var best = mutableSetOf<String>()
        for ((pc1, conns) in input) {
            val current = mutableSetOf(pc1)
            for (pc2 in conns) {
                if (input[pc2]!!.containsAll(current)) current += pc2
            }
            if (current.size > best.size) best = current
        }
        return best.sorted().joinToString(",")
    }

}

fun main() {
    Harness.run(Day23)
}
