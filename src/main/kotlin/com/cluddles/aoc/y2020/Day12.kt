package com.cluddles.aoc.y2020

import com.cluddles.aoc.util.Dir8
import com.cluddles.aoc.util.Int2d
import java.io.File

class Day12 {

    // Parse line into op-arg pair; convert L (anticlockwise) into R (clockwise) with appropriately munged arg
    private fun parseLine(line: String) : Pair<Char, Int> {
        val result = Pair(line.first(), line.drop(1).toInt())
        return if (result.first == 'L') Pair('R', 360 - result.second) else result
    }

    fun solvePart1(src: File) : Int {
        var pos = Int2d(0, 0)
        var dir = Dir8.E
        src.forEachLine {
            val (op, arg) = parseLine(it)
            when (op) {
                'F' -> pos += dir.delta * arg
                'R' -> dir = dir.rotate(arg / 45)
                else -> pos += Dir8.valueOf(op.toString()).delta * arg
            }
        }
        return pos.manhattan()
    }

    fun solvePart2(src: File) : Int {
        var pos = Int2d(0, 0)
        var waypoint = Int2d(10, -1)
        src.forEachLine {
            val (op, arg) = parseLine(it)
            when (op) {
                'F' -> pos += waypoint * arg
                'R' -> for (i in 0 until arg step 90) waypoint = Int2d(-waypoint.y, waypoint.x)
                else -> waypoint += Dir8.valueOf(op.toString()).delta * arg
            }
        }
        return pos.manhattan()
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day12()
            val file = File("aoc-secret/2020/day12")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
