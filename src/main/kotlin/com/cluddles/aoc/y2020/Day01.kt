package com.cluddles.aoc.y2020

import java.io.File

class Day01 {

    fun readInput(file: File) : Set<Int> {
        val fileContents = file.readLines()
        return HashSet(fileContents.map(Integer::valueOf))
    }

    private fun sumOfTwo(input: Set<Int>, target: Int) : List<Int> {
        return input.filter { input.contains(target - it ) }
    }
    private fun sumOfThree(input: Set<Int>, target: Int) : List<Int> {
        return input.filter { sumOfTwo(input, target - it).isNotEmpty() }
    }

    fun solvePart1(input: Set<Int>) : Int {
        return sumOfTwo(input, 2020).let {it[0] * it[1]}
    }
    fun solvePart2(input: Set<Int>) : Long {
        return sumOfThree(input, 2020).let {it[0].toLong() * it[1] * it[2]}
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day01()
            val input = solver.readInput(File("aoc-secret/2020/day01"))
            println(solver.solvePart1(input))
            println(solver.solvePart2(input))
        }
    }

}
