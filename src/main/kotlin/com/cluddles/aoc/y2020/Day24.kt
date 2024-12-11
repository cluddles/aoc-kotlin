package com.cluddles.aoc.y2020

import java.io.File

class Day24 {

    data class Vec3(val x: Int, val y: Int, val z: Int) {
        operator fun plus(o: Vec3) : Vec3 { return Vec3(x + o.x, y + o.y, z + o.z) }
        operator fun times(o: Int) : Vec3 { return Vec3(x * o, y * o, z * o) }
    }

    // We're working with hexes, so time for a link to the best docs I've found on this stuff:
    // https://www.redblobgames.com/grids/hexagons/

    // Short version: you can use Axial (2d) or Cube (3d) coordinate systems
    // Axial is more efficient for serialisation, Cube tends to be easier for actual processing

    // We have a "horizontal" layout here, i.e. E and W are directions (vertical would have N and S)
    // Plane where x + y + z == 0
    enum class Dir(val delta: Vec3) {
        NE(Vec3(1, 0, -1)),
        E (Vec3(1, -1, 0)),
        SE(Vec3(0, -1, 1)),
        SW(Vec3(-1, 0, 1)),
        W (Vec3(-1, 1, 0)),
        NW(Vec3(0, 1, -1)),
    }

    data class Path(val dirs: List<Dir>)

    // Convert line to list of dirs it represents
    fun parseLine(line: String) : Path {
        // A slight faff because some dirs are one char and some two
        var remain = line
        val result = ArrayList<Dir>()
        while (remain.isNotEmpty()) {
            result.add(
                when {
                    remain.startsWith("e") -> Dir.E
                    remain.startsWith("w") -> Dir.W
                    else -> Dir.valueOf(remain.take(2).toUpperCase())
                }
            )
            remain = remain.drop(result.last().name.length)
        }
        return Path(result)
    }

    fun parse(src: File) : List<Path> = src.readLines().map { parseLine(it) }

    // Read the input
    // Flip the tile described by each path (line) from white to black (or back again if already flipped)
    fun initialState(src: File) : Set<Vec3> {
        val blackTiles = HashSet<Vec3>()
        parse(src).forEach {
            val dest = it.dirs.fold(Vec3(0, 0, 0), { a, b -> a + b.delta } )
            // Add if not present, remove otherwise
            if (!blackTiles.add(dest)) blackTiles.remove(dest)
        }
        return blackTiles
    }

    // List of adjacent positions for a given point
    private fun adjacents(pos: Vec3) = Dir.values().map { pos + it.delta }
    // Count the number of black tiles present in positions adjacent to the given point
    private fun countAdjacentBlackTiles(pos: Vec3, blackTiles: Set<Vec3>) = Dir.values()
        .map { pos + it.delta }
        .count { blackTiles.contains(it) }

    // Part 2: Apply a single day iteration
    fun processDay(blackTiles: Set<Vec3>) : Set<Vec3> {
        val result = HashSet<Vec3>(blackTiles)
        // * Any black tile with zero or more than 2 black tiles immediately adjacent to it is flipped to white.
        blackTiles.forEach { tile -> if (countAdjacentBlackTiles(tile, blackTiles) !in 1..2) result.remove(tile) }
        // * Any white tile with exactly 2 black tiles immediately adjacent to it is flipped to black.
        val relevantWhiteTiles = blackTiles.flatMap { adjacents(it) }.filter { !blackTiles.contains(it) }.toSet()
        relevantWhiteTiles.forEach { tile -> if (countAdjacentBlackTiles(tile, blackTiles) == 2) result.add(tile) }
        return result
    }
    // Apply the given number of daily iterations to the initial state
    fun simulateDays(src: File, numDays: Int) : Set<Vec3> {
        var state = initialState(src)
        (1..numDays).forEach { state = processDay(state) }
        return state
    }

    fun solvePart1(src: File) = initialState(src).size
    fun solvePart2(src: File) = simulateDays(src, 100).size

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day24()
            val file = File("aoc-secret/2020/day24")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
