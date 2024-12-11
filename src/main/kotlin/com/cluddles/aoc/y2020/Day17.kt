package com.cluddles.aoc.y2020

import java.io.File

class Day17 {

    interface Pos {
        fun adjacents() : List<Pos>
    }

    // Position in 3D space
    data class Pos3(val x: Int, val y: Int, val z: Int) : Pos {
        override fun adjacents() : List<Pos> =
            (-1..1).flatMap { i ->
                (-1..1).flatMap { j ->
                    (-1..1)
                        .filter { k -> i != 0 || j != 0 || k != 0 }
                        .map { k -> Pos3(x + i, y + j, z + k) }
                }
            }
    }

    // Position in 4D space
    data class Pos4(val x: Int, val y: Int, val z: Int, val w: Int) : Pos {
        override fun adjacents(): List<Pos> =
            (-1..1).flatMap { i ->
                (-1..1).flatMap { j ->
                    (-1..1).flatMap { k ->
                        (-1..1)
                            .filter { l -> i != 0 || j != 0 || k != 0 || l != 0 }
                            .map { l -> Pos4(x + i, y + j, z + k, w + l) }
                    }
                }
            }
    }

    private fun simulate(cubes: Set<Pos>) : Set<Pos> {
        // Need to process all cubes and any adjacents
        val toProcess = HashSet<Pos>()
        for (p in cubes) {
            toProcess.add(p)
            toProcess.addAll(p.adjacents())
        }
        // Now we can just look at each cube, count adjacents and build up result as appropriate
        val result = HashSet<Pos>()
        for (p in toProcess) {
            p.adjacents().count { cubes.contains(it) }.let {
                when {
                    cubes.contains(p) && (it == 2 || it == 3) -> result.add(p)
                    !cubes.contains(p) && it == 3 -> result.add(p)
                    else -> {}
                }
            }
        }
        return result
    }

    private fun solve(src: File, dimensions: Int) : Int {
        val cubes = HashSet<Pos>()
        src.readLines().forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') cubes.add(
                    when (dimensions) {
                        3 -> Pos3(x, y, 0)
                        4 -> Pos4(x, y, 0, 0)
                        else -> throw IllegalArgumentException("Unsupported dimensions: $dimensions")
                    }
                )
            }
        }
        var next: Set<Pos> = cubes
        (1..6).forEach { _ -> next = simulate(next) }
        return next.size
    }

    fun solvePart1(src: File) : Int = solve(src, 3)
    fun solvePart2(src: File) : Int = solve(src, 4)

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day17()
            val file = File("aoc-secret/2020/day17")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
