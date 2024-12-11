package com.cluddles.aoc.y2020

import java.io.File

class Day05 {

    fun seatId(bsp: String) : Int {
        // It's... just binary with different digits
        val bin = bsp.replace("B", "1")
            .replace("F", "0")
            .replace("R", "1")
            .replace("L", "0")
        return Integer.parseInt(bin, 2);
    }

    fun solvePart1(src: File) : Int {
        return src.readLines().maxOf { seatId(it) }
    }

    fun solvePart2(src: File) : Int {
        val allSeatIds = HashSet(src.readLines().map { seatId(it) })
        // First taken seat where next seat is free and seat after is taken
        // We're interested in the free seat so don't forget to add 1 to the result! (Yes, I did)
        return allSeatIds.first { !allSeatIds.contains(it+1) && allSeatIds.contains(it+2) } + 1
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day05()
            val file = File("aoc-secret/2020/day05")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
