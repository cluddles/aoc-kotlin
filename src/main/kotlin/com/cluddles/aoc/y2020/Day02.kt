package com.cluddles.aoc.y2020

import java.io.File

class Day02 {

    private val elRex = Regex("(\\d+)-(\\d+) (\\w): (\\w*)")

    private data class Element(
        val v1: Int,
        val v2: Int,
        val letter: Char,
        val password: String
    )

    private fun readInput(src: File) : List<Element> {
        return src.readLines().map { line -> parseLine(line) }
    }

    private fun parseLine(line: String) : Element {
        val res = elRex.matchEntire(line)!!
        return Element(
            res.groupValues[1].toInt(),
            res.groupValues[2].toInt(),
            res.groupValues[3][0],
            res.groupValues[4])
    }

    fun solvePart1(src: File) : Int {
        return readInput(src)
            .filter { el ->
                val count = el.password.count{ it == el.letter }
                count <= el.v2 && count >= el.v1
            }
            .count()
    }

    fun solvePart2(src: File) : Int {
        return readInput(src)
            .filter { el ->
                (el.password[el.v1 - 1] == el.letter) != (el.password[el.v2 - 1] == el.letter)
            }
            .count()
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day02()
            val file = File("aoc-secret/2020/day02")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
