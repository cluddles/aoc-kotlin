package com.cluddles.aoc.y2020

import java.io.File

class Day03 {

    private fun readInput(src: File) : List<String> {
        return src.readLines()
    }

    private fun traverse(lines: List<String>, xStep: Int, yStep: Int) : Int {
        var x = 0
        var y = 0
        var numTrees = 0
        while (true) {
            x = (x + xStep) % lines[0].length
            y += yStep
            if (y >= lines.size) return numTrees
            if (lines[y][x] == '#') numTrees++
        }
    }

    fun solvePart1(src: File) : Int {
        return traverse(readInput(src), 3, 1)
    }

    fun solvePart2(src: File) : Long {
        val lines = readInput(src)
        return traverse(lines, 1, 1).toLong() *
                traverse(lines, 3, 1) *
                traverse(lines, 5, 1) *
                traverse(lines, 7, 1) *
                traverse(lines, 1, 2)
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day03()
            val file = File("aoc-secret/2020/day03")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
