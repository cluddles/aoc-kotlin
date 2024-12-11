package com.cluddles.aoc.y2020

import com.cluddles.aoc.util.Dir8
import com.cluddles.aoc.util.Int2d
import java.io.File

class Day11 {

    private data class SeatMap(val width: Int, val height: Int, val seats: MutableMap<Int2d, Seat> = HashMap())

    private enum class Seat { EMPTY, OCCUPIED }

    // Map of position -> seat state
    private fun parse(src: File) : SeatMap {
        val lines = src.readLines()
        val plan = SeatMap(lines.first().length, lines.size)
        for ((y, line) in lines.withIndex()) {
            for ((x, c) in line.withIndex()) {
                if (c == 'L') plan.seats[Int2d(x, y)] = Seat.EMPTY
            }
        }
        return plan
    }

    private fun adjacentPoints(p: Int2d) : List<Int2d> {
        return Dir8.values().map { p + it.delta }
    }

    // Check seats in 8 adjacent cells
    private fun seatTickAdjacent(pos: Int2d, seat: Seat, map: SeatMap) : Seat {
        val adj = adjacentPoints(pos).filter { map.seats[it] == Seat.OCCUPIED }.count()
        return when {
            adj == 0 -> Seat.OCCUPIED
            adj >= 4 -> Seat.EMPTY
            else -> seat
        }
    }

    private fun look(map: SeatMap, pos: Int2d, delta: Int2d) : Seat? {
        val newPos = pos + delta
        if (newPos.x < 0 || newPos.y < 0 || newPos.x >= map.width || newPos.y >= map.height) return null
        return map.seats[newPos] ?: look(map, newPos, delta)
    }

    // Look along each of the 8 directions for first seat
    private fun seatTickBeam(pos: Int2d, seat: Seat, map: SeatMap) : Seat {
        val adj = Dir8.values().filter { look(map, pos, it.delta) == Seat.OCCUPIED }.count()
        return when {
            adj == 0 -> Seat.OCCUPIED
            adj >= 5 -> Seat.EMPTY
            else -> seat
        }
    }

    private fun seatMapTick(map: SeatMap, seatTickFunc: (a: Int2d, b: Seat, c: SeatMap) -> Seat) : SeatMap {
        val next = SeatMap(map.width, map.height)
        map.seats.entries.forEach { next.seats[it.key] = seatTickFunc(it.key, it.value, map) }
        return next
    }

    private fun solve(seatMap: SeatMap, seatTickFunc: (a: Int2d, b: Seat, c: SeatMap) -> Seat) : Int {
        var next: SeatMap? = null
        do {
            val current = next ?: seatMap
            next = seatMapTick(current, seatTickFunc)
        } while (current != next)
        return next.seats.filter { it.value == Seat.OCCUPIED }.count()
    }

    fun solvePart1(src: File) : Int {
        return solve(parse(src), ::seatTickAdjacent)
    }

    fun solvePart2(src: File) : Int {
        return solve(parse(src), ::seatTickBeam)
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day11()
            val file = File("aoc-secret/2020/day11")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
