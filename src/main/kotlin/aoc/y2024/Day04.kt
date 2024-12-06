package aoc.y2024

import aoc.core.Harness
import aoc.core.Resource
import aoc.core.Solver
import aoc.util.CharGrid
import aoc.util.Dir8

/** Ceres Search */
object Day04: Solver<CharGrid, Int> {

    override fun prepareInput(path: String): CharGrid {
        return CharGrid(Resource.asLines(path), ' ')
    }

    /**
     * For [input] at pos [x],[y], checks all 8 possible directions for [target] word
     *
     * @return The number of times the word starts at given position
     */
    private fun scanForWord(input: CharGrid, x: Int, y: Int, target: String = "XMAS"): Int {
        // Check for the first letter, abort early
        if (input[x, y] != target[0]) return 0
        val maxOffset = target.length - 1
        var result = 0
        dirs@ for (dir in Dir8.entries) {
            // Don't bother checking this direction if the word can't possibly fit
            if (!input.isInBounds(x + dir.delta.x * maxOffset, y + dir.delta.y * maxOffset)) continue
            // Check for each letter
            for (o in 1..maxOffset) {
                if (input[x + dir.delta.x * o, y + dir.delta.y * o] != target[o]) continue@dirs
            }
            result++
        }
        return result
    }

    /**
     * For [input] at pos [x],[y], checks for middle letter of [target]; then checks for adjacent diagonals containing
     * first and last letters of [target] such that they form a cross.
     *
     * @return If two instances of the word cross over at the given position
     */
    private fun scanForCross(input: CharGrid, x: Int, y: Int, target: String = "MAS"): Boolean {
        // Check for the middle letter, abort early
        if (input[x, y] != target[1]) return false
        // NW has to be either first or last letter
        val nw = input[x-1, y-1]
        if (nw != target[0] && nw != target[2]) return false
        // Set `a` to NW, `b` to what we want SE to be
        val a = nw
        val b = if (a == target[0]) target[2] else target[0]
        val ne = input[x+1, y-1]
        val se = input[x+1, y+1]
        val sw = input[x-1, y+1]
        // NE or SW has to be `a`
        // SE has to be `b`
        // NE or SW has to be `b`
        return (ne == a || sw == a) && se == b && (sw == b || ne == b)
    }

    override fun solvePart1(input: CharGrid): Int {
        // Look for "X", and then scan surrounding 8 dirs for "MAS"
        var result = 0
        for (i in 0 until input.width) {
            for (j in 0 until input.height) {
                result += scanForWord(input, i, j)
            }
        }
        return result
    }

    override fun solvePart2(input: CharGrid): Int {
        // Look for "A", and then scan adjacent diagonals for valid configuration of "M" and "S"
        var result = 0
        for (i in 1 until input.width-1) {
            for (j in 1 until input.height-1) {
                if (scanForCross(input, i, j)) result++
            }
        }
        return result
    }

}

fun main() {
    Harness.run(Day04, "/2024/day04")
}
