package com.cluddles.aoc.y2020

import java.io.File

class Day10 {

    private fun parseAdapters(src: File) : List<Int> {
        // Note there are no duplicate adapters
        val result = ArrayList<Int>()
        src.forEachLine { result.add(it.toInt()) }
        // Manually add the 0 outlet and the +3 device adapter
        result.add(0)
        result.add(result.maxOf { it } + 3)
        // All solutions rely on this being sorted
        return result.sorted()
    }

    fun countDiffs(list: List<Int>) : IntArray {
        val result = IntArray(4)
        for (i in 1 until list.size) result[list[i] - list[i-1]]++
        return result
    }

    fun solvePart1(src: File) : Int {
        return countDiffs(parseAdapters(src)).let { it[1] * it[3] }
    }

    fun solvePart2(src: File) : Long {
        val adapters = parseAdapters(src)
        // Track the number of valid ways you can reach each value.
        // IT'S BIG so use a long unless you enjoy getting the wrong answer...
        val paths = LongArray(adapters.size)
        paths[0] = 1
        for (i in adapters.indices) {
            for (j in i+1 until adapters.size) {
                if (adapters[j] - adapters[i] <= 3) paths[j] += paths[i] else break
            }
        }
        return paths[adapters.size-1]
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day10()
            val file = File("aoc-secret/2020/day10")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
