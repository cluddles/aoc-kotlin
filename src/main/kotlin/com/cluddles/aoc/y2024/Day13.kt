package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.Int2d

/** Claw Contraption */
object Day13: Solver<List<Day13.Scenario>, Int> {

    data class Scenario(val a: Int2d, val b: Int2d, val prize: Int2d)

    override fun prepareInput(src: SolverInput): List<Scenario> {
        val result = mutableListOf<Scenario>()
        val it = src.lines().iterator()
        while (it.hasNext()) {
            result.add(Scenario(
                parseLine(it.next()),
                parseLine(it.next()),
                parseLine(it.next())
            ))
        }
        return result
    }

    private val regex = Regex("[+=](\\d+).+[+=](\\d+)")

    private fun parseLine(text: String): Int2d {
        val r = regex.find(text)!!
        return Int2d(r.groupValues[1].toInt(), r.groupValues[2].toInt())
    }

    private fun isSolution(scenario: Scenario, a: Int, b: Int): Boolean {
        return scenario.prize.x == (scenario.a.x * a + scenario.b.x * b)
                && scenario.prize.y == (scenario.a.y * a + scenario.b.y * b)
    }

    private fun bruteForce(scenario: Scenario): Int {
        // Why not just brute force it
        var best = -1
        for (a in 0..100) {
            for (b in 0..100) {
                if (isSolution(scenario, a, b)) {
                    val score = a*3 + b
                    if (best == -1 || score < best) {
                        best = score
                    }
                }
            }
        }
        return best
    }

    override fun solvePart1(input: List<Scenario>): Int {
        return input.map { bruteForce(it) }.filter { it != -1 }.sum()
    }

    private fun solve(s: Scenario): Int {
        val offset = 10000000000000L
        val x = s.prize.x + offset
        val y = s.prize.y + offset

        // it's "just" simultaneous equations
        // Shame I'm rusty as heck

        // 1. x1 * a + x2 * b = px + offset
        // 2. y1 * a + y2 * b = py + offset
        // (* (y2 / x2)) on 1: (x1 * y2 * a) / x2 + (y2 * x2) / x2 = (px + offset) * y2 / x2
        //                     (x1 * y2 * a) / x2 + y2 = (px + offset) * y2 / x2
        // 1 - 2
        // (x1 * y2 * a) / x2 - (y1 * a) = ((px + offset) * y2) / x2 - py + offset
        // (x1 * y2 * a - y1 * a * x2) / x2 = ((px + offset) *




        // solve for a:
        //   1. x1 * a = px + offset - x2 * b
        //           a = (px + offset) / x1 - (x2 * b) / x1
        //   2. y1 * a = py + offset - y2 * b
        //           a = (py + offset) / y1 - (y2 * b) / y1
        //   mult y2, div x2
        //



        val a = (y - (x * s.b.y / s.b.x)) / (s.a.y - (s.b.y / s.a.x) / s.b.x)
        val b = (x - s.a.x * a) / s.b.x

        // Presumably we only want non-fractional results :P
        // So we'd need (x * s.b.y / s.b.x) to have remainder 0? But not, because things cancel out.
        // Maybe this can be simplified more (e.g. top and bottom both doing / s.b.x)

        return (a*3 + b).toInt()
    }


    override fun solvePart2(input: List<Scenario>): Int {
        // Button A: X+94, Y+34
        // Button B: X+22, Y+67
        // Prize: X=10000000008400, Y=10000000005400

        // 94a + 22b = 10000000008400 (x)
        // 22b = x - 94a
        // b = (x - 94a) / 22

        // 34a + 67b = 10000000005400 (y)
        // 34a + 67 ((x - 94a) / 22) = y
        // 34a + 67/22 x - (67*94 / 22) a = y
        // 34a - (67*94 / 22) a = y - 67/22 x
        // a = (y - 67/22 x) / (34 - (67*94) / 22)




        return input.map { solve(it) }.filter { it != -1 }.sum()


        TODO("Not yet implemented")
    }

}

fun main() {
    val x = 10000000008400L
    val y = 10000000005400L
    val res = (y - (67/22f) * x) / (34 - (67 * 94) / 22f)
    println(res)

    println(3000000002960L / 37f)

    Harness.run(Day13)
}
