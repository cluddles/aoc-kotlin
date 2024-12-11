package com.cluddles.aoc.y2020

import java.io.File

class Day09 {

    private fun parse(src: File) : ArrayList<Long> {
        val result = ArrayList<Long>()
        src.forEachLine { result.add(it.toLong()) }
        return result
    }

    private fun isValid(list: ArrayList<Long>, pos: Int, previous: Int) : Boolean {
        for (i in pos - previous until pos - 1) {
            for (j in i until pos) {
                if (list[i] + list[j] == list[pos]) return true
            }
        }
        return false
    }

    private fun findInvalid(list: ArrayList<Long>, preamble: Int) : Long {
        for (i in preamble until list.size) {
            if (!isValid(list, i, preamble)) return list[i]
        }
        throw IllegalStateException("No solution")
    }

    private fun findSum(list: ArrayList<Long>, target: Long) : List<Long> {
        for (i in 0 until list.size-1) {
            // Sum must be at least 2 elements; assume there's nothing <= 0 in the list
            if (list[i] >= target) continue
            var acc = target - list[i]
            for (j in i+1 until list.size) {
                acc -= list[j]
                if (acc == 0L) return list.slice(i..j)
                if (acc < 0L) break
            }
        }
        throw IllegalStateException("No solution")
    }

    fun solvePart1(src: File, preamble: Int) : Long {
        return findInvalid(parse(src), preamble)
    }

    fun solvePart2(src: File, target: Long) : Long {
        return findSum(parse(src), target)
            .sorted()
            .let { it[0] + it[it.size-1] }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day09()
            val file = File("aoc-secret/2020/day09")
            val invalid = solver.solvePart1(file, 25)
            println(invalid)
            println(solver.solvePart2(file, invalid))
        }
    }

}
