package com.cluddles.aoc.y2020

import java.io.File

class Day25 {

    // Transform for a single iteration
    fun transformOnce(value: Long, subject: Long) : Long = value * subject % 20201227

    // Transform for loopSize iterations
    fun transform(loopSize: Int, subject: Long) : Long {
        var r = 1L
        (1..loopSize).forEach { _ -> r = transformOnce(r, subject) }
        return r
    }

    // Transform until target is reached, return loopSize
    fun findLoopSize(target: Long, subject: Long) : Int {
        var r = 1L
        var loopSize = 0
        while (r != target) {
            r = transformOnce(r, subject)
            loopSize++
        }
        return loopSize
    }

    fun solve(keys: List<Long>) = transform(findLoopSize(keys[0], 7), keys[1])

    fun solvePart1(src: File) = solve(src.readLines().map { it.toLong() })

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day25()
            val file = File("aoc-secret/2020/day25")
            println("Part1: " + solver.solvePart1(file))
            // There is no part 2
        }
    }

}
