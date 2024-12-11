package com.cluddles.aoc.y2020

import com.cluddles.aoc.util.Dir8
import com.cluddles.aoc.util.Int2d
import java.io.File
import kotlin.math.roundToInt
import kotlin.math.sqrt

class Day20 {

    private val snek = listOf(
        "                  # ",
        "#    ##    ##    ###",
        " #  #  #  #  #  #   ",
    )

    // Permutations for tiled data are:
    //   all 4 rotations
    //   flipX + all 4 rotations

    data class Tile(val id: Int, val data: List<String>) {
        val edges = listOf(
            data.first(),
            data.map { it.last() }.joinToString(""),
            data.last(),
            data.map { it.first() }.joinToString("")
        )

        private fun rotate() = Tile(id, Composite.rotate(data))

        private fun flipX() = Tile(id, Composite.flipX(data))

        fun permutations() : List<Tile> {
            val result = arrayListOf(this)
            (1..3).forEach { _ -> result.add(result.last().rotate()) }
            result.add(flipX())
            (1..3).forEach { _ -> result.add(result.last().rotate()) }
            return result
        }

    }

    data class Composite(var data: List<String>) {
        var permutation = 0;
        fun nextPermutation() : Boolean {
            if (permutation >= 7) return false
            data = rotate(data)
            if (permutation == 4) data = flipX(data)
            permutation++
            return true
        }

        // Make these available to non-Tiles to use
        companion object {
            fun rotate(data: List<String>): List<String> =
                data.indices.map { j ->
                    data.first().indices.map { i ->
                        data[data.first().length - 1 - i][j]
                    }.joinToString("")
                }

            fun flipX(data: List<String>): List<String> = data.map { it.reversed() }
        }
    }

    // Map of tile id to tile
    fun parse(src: File) : Map<Int, Tile> {
        val lines = src.readLines()
        // Tile: xxxx, then 10 lines of bitmap data, then blank line
        return (lines.indices step 12)
            .map { parseTile(lines.slice(it..it+10)) }
            .map { Pair(it.id, it) }
            .toMap()
    }

    // Tile id from first line, char data from the others
    fun parseTile(lines: List<String>) : Tile = Tile(lines[0].substring(5, lines[0].length - 1).toInt(), lines.drop(1))

    // Attempts to arrange the given tiles
    // Assumes there's only one place that each tile will fit - apparently this is the case
    fun arrange(tiles: Map<Int, Tile>) : List<List<Tile>> {
        var unplaced = tiles.values
        val board = HashMap<Int2d, Tile>()
        while (unplaced.isNotEmpty()) {
            unplaced = unplaced.filter { !place(it, board) }
        }
        val min = Int2d(board.keys.minOf { it.x }, board.keys.minOf { it.y })
        val len = sqrt(tiles.size.toDouble()).roundToInt()
        return (min.y until min.y + len).map { j ->
            (min.x until min.x + len).map { i ->
                board[Int2d(i, j)]!!
            }
        }
    }

    // Attempts to place a tile onto a board of other tiles
    fun place(tile: Tile, board: HashMap<Int2d, Tile>) : Boolean {
        // Place starting tile
        if (board.isEmpty()) {
            board[Int2d(0, 0)] = tile
            return true
        }
        // For each subsequent tile, generate 8 possible permutations and try to place onto board
        val permutations = tile.permutations()
        for (i in permutations) {
            for (j in board.entries) {
                val dir = when {
                    i.edges[0] == j.value.edges[2] -> Dir8.S
                    i.edges[1] == j.value.edges[3] -> Dir8.W
                    i.edges[2] == j.value.edges[0] -> Dir8.N
                    i.edges[3] == j.value.edges[1] -> Dir8.E
                    else -> continue
                }
                board[j.key + dir.delta] = i
                return true
            }
        }
        return false
    }

    // Strip tile edges and combine data
    // Init permutation state
    fun composite(layout: List<List<Tile>>) : Composite = Composite(
        layout.flatMap { j ->
            (1..8).map { y ->
                j.joinToString("") { it.data[y].drop(1).dropLast(1) }
            }
        }
    )

    // Looks for pattern in the given composite
    // Modifies the composite to keep track
    fun findPattern(composite: Composite, pattern: List<String>) {
        // Convert to/from char array for this function
        // It's hacky, but I can't face trying to refactor everything to use CharArrays now
        val data = composite.data.map { it.toCharArray() }
        for (i in 0 until data.size - pattern.size) {
            cellLoop@ for (j in 0 until data.first().size - pattern.first().length) {
                for (m in pattern.indices) {
                   for (n in pattern.first().indices) {
                        if (pattern[m][n] == ' ') continue
                        if (data[i+m][j+n] == '.') continue@cellLoop
                    }
                }

                for (m in pattern.indices) {
                    for (n in pattern.first().indices) {
                        if (pattern[m][n] == ' ') continue
                        data[i+m][j+n] = 'O'
                    }
                }
            }
        }
        composite.data = data.map { it.joinToString("") }
    }

    fun solvePart1(src: File) : Long {
        // Arrange all tiles
        val layout = arrange(parse(src))
        // Multiply id of all 4 corner tiles
        return listOf(layout.first(), layout.last())
            .map { it.first().id.toLong() * it.last().id }
            .fold(1L, { a, b -> a * b } )
    }

    fun solvePart2(src: File) : Int {
        // Arrange all tiles, generate composite, search for "sea monsters"
        val layout = arrange(parse(src))
        val composite = composite(layout)
        do findPattern(composite, snek) while (composite.nextPermutation())
        // Return all # cells that are not part of snek
        return composite.data.sumOf { line -> line.count { c -> c == '#' }}
    }

    companion object {

        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day20()
            val file = File("aoc-secret/2020/day20")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))

        }
    }

}
