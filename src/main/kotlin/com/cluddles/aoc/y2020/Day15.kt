package com.cluddles.aoc.y2020

class Day15 {

    private fun solve(src: String, repetitions: Int) : Int {
        // Remember numbers spoken; number -> time (1-indexed)
        // Execution takes ~10 times longer if you use a proper map, so...
        val times = IntArray(repetitions)
        val input = src.split(",").map { it.toInt() }
        input.forEachIndexed { index, num -> times[num] = index + 1 }
        var prev = input.last()
        for (step in input.size until repetitions) {
            val prevTime = times[prev]
            times[prev] = step
            prev = if (prevTime == 0) 0 else step - prevTime
        }
        return prev
    }

    fun solvePart1(src: String) : Int = solve(src, 2020)
    fun solvePart2(src: String) : Int = solve(src, 30000000)

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day15()
            val input = "19,20,14,0,9,1"
            println("Part1: " + solver.solvePart1(input))
            println("Part2: " + solver.solvePart2(input))
        }
    }

}
