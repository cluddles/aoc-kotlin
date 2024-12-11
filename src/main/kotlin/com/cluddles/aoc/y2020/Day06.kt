package com.cluddles.aoc.y2020

import java.io.File

class Day06 {

    private class Group {
        val answers = IntArray(26)
        var rows = 0
    }

    private fun parse(src: File) : List<Group> {
        val result = ArrayList<Group>()
        result.add(Group())
        src.forEachLine { line ->
            if (line.isBlank()) {
                result.add(Group())
            } else {
                for (c in line.toCharArray()) result.last().answers[c - 'a']++
                result.last().rows++
            }
        }
        return result
    }

    fun solvePart1(src: File) : Int {
        // Answer contained by any row
        return parse(src)
            .sumBy { grp ->
                grp.answers.filter { it != 0 }.count()
            }
    }

    fun solvePart2(src: File) : Int {
        // Answer contained by all rows
        return parse(src)
            .sumBy { grp ->
                grp.answers.filter { it == grp.rows }.count()
            }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day06()
            val file = File("aoc-secret/2020/day06")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
