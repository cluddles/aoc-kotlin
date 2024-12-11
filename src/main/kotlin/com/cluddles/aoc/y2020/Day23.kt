package com.cluddles.aoc.y2020

class Day23 {

    private fun inVal(input: String, pos: Int) : Int {
        return if (pos < input.length) Character.getNumericValue(input[pos]) else pos+1
    }

    private fun solve(input: String, numCups: Int, numIterations: Int) : IntArray {
        // Based on the suggestion:
        // "the nth index is the label of the cup that comes after the cup labeled n"
        // Ignore index 0, it's meaningless (we have no 0 in the input)
        val cups = IntArray(numCups + 1)
        var current = 0
        (0 until numCups).forEach {
            cups[current] = inVal(input, it)
            current = cups[current]
        }
        // Make it loop correctly, set the initial cup to the right one
        cups[current] = inVal(input, 0)
        current = cups[current]

        (1..numIterations).forEach {
            // Destination is current-1
            var dest = current - 1
            // Take the 3 clockwise cups
            val p1 = cups[current]
            val p2 = cups[p1]
            val p3 = cups[p2]
            cups[current] = cups[p3]
            // Make sure the destination is available
            while (p1 == dest || p2 == dest || p3 == dest || dest < 1) {
                dest--
                if (dest < 1) dest = numCups
            }
            // Insert picked up cups at destination
            val n = cups[dest]
            cups[dest] = p1
            cups[p3] = n
            // Seek to the next cup after current
            current = cups[current]
        }
        return cups
    }

    fun solvePart1(input: String) : String {
        // Cups clockwise after 1
        val cups = solve(input, input.length, 100)
        var current = cups[1]
        var result = ""
        while (current != 1) {
            result += current
            current = cups[current]
        }
        return result
    }

    // Multiply the two cups that are clockwise from 1
    fun solvePart2(input: String) = solve(input, 1_000_000, 10_000_000).let { it[1].toLong() * it[it[1]] }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day23()
            val input = "963275481"
            println("Part1: " + solver.solvePart1(input))
            println("Part2: " + solver.solvePart2(input))
        }
    }

}
