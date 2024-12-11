package com.cluddles.aoc.y2020

import java.io.File

class Day13 {

    fun solvePart1(src: File) : Int {
        val (earliest, ids) = src.readLines().let { lines ->
            Pair(
                lines[0].toInt(),
                lines[1]
                    .split(",")
                    .filter { it != "x" }
                    .map { it.toInt() }
            )
        }
        return ids
            .map { Pair(it, it - (earliest % it)) }
            .minByOrNull { it.second }!!
            .let { it.first * it.second }
    }

    fun solvePart2(src: File) : Long {
        // Read pairs: first=id, second=offset
        val idOffsets = src.readLines()[1]
            .split(",")
            .withIndex()
            .filter { it.value != "x" }
            .map { Pair(it.value.toLong(), it.index.toLong()) }
            .sortedByDescending { it.first }
        // Brute force is super slow, unsurprisingly
        // We can be smarter - start with timestamp+step pair that work for the least frequent bus
        // When the next bus comes into alignment, adjust step accordingly so that future values work for both buses
        var step = idOffsets.first().first
        var timestamp = step - idOffsets.first().second
        var idOffsetsRemaining = idOffsets.drop(1)
        while (true) {
            // Bring next bus into alignment
            while (idOffsetsRemaining.first().let { (timestamp + it.second) % it.first == 0L }) {
                if (idOffsetsRemaining.size == 1) return timestamp
                println("Pop $idOffsetsRemaining at timestamp=$timestamp, step=$step")
                // Update step - we can do dumb multiplication here because all the numbers are prime
                step *= idOffsetsRemaining.first().first
                idOffsetsRemaining = idOffsetsRemaining.drop(1)
            }
            timestamp += step
        }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day13()
            val file = File("aoc-secret/2020/day13")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
