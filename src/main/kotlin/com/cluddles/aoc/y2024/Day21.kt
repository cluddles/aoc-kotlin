package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.util.Int2d
import kotlin.math.absoluteValue
import kotlin.text.mapIndexed

/** Keypad Conundrum */
object Day21: Solver<List<String>, Long> {

    override fun prepareInput(src: SolverInput): List<String> {
        return src.lines().toList()
    }

    /** Convert list of keys into map of key to position, assuming keypad of width 3 */
    private fun keypadLayout(keys: String) = keys
            .mapIndexed { i, ch -> ch to Int2d(i % 3, i / 3) }
            .filter { it.first != ' ' }
            .toMap()

    /**
     * Numeric keypad layout
     * ```
     * +---+---+---+
     * | 7 | 8 | 9 |
     * +---+---+---+
     * | 4 | 5 | 6 |
     * +---+---+---+
     * | 1 | 2 | 3 |
     * +---+---+---+
     *     | 0 | A |
     *     +---+---+
     * ```
     */
    val numPadKeys = keypadLayout("789456123 0A")

    /**
     * Directional keypad layout
     * ```
     *     +---+---+
     *     | ^ | A |
     * +---+---+---+
     * | < | v | > |
     * +---+---+---+
     * ```
     */
    val dirPadKeys = keypadLayout(" ^A<v>")

    /** Convenience function to repeat string [times] if positive */
    private fun String.repeatSafe(times: Int) = if (times > 0) repeat(times) else ""

    /** For a numeric pad [seq] (e.g. `123A`), this will return one shortest directional pad input for parent robot */
    private fun numPadToDirs(seq: String, startFrom: Char = 'A'): String {
        return seq.indices
            .joinToString("A") { i -> numPath(if (i == 0) startFrom else seq[i - 1], seq[i]) } + "A"
    }

    private fun numPath(from: Char, to: Char): String {
        val prevPos = numPadKeys[from] ?: error("Bad key: $from")
        val nextPos = numPadKeys[to] ?: error("Bad key: $to")
        // Exception: if starting from bottom row, you cannot go left over the "gap"
        val delta = nextPos - prevPos
        if (prevPos.y == 3 && nextPos.x == 0) {
            return "^".repeatSafe(delta.y.absoluteValue) + "<".repeatSafe(delta.x.absoluteValue)
        } else if (prevPos.x == 0 && nextPos.y == 3) {
            return ">".repeatSafe(delta.x.absoluteValue) + "v".repeatSafe(delta.y.absoluteValue)
        }
        return "<".repeatSafe(-delta.x) + "v".repeatSafe(delta.y) + "^".repeatSafe(-delta.y) + ">".repeatSafe(delta.x)
    }

    /** For a directional pad transition, returns optimal path to move between keys */
    private fun dirPath(from: Char, to: Char): String {
        val prevPos = dirPadKeys[from] ?: error("Bad key: $from")
        val nextPos = dirPadKeys[to] ?: error("Bad key: $to")
        val delta = nextPos - prevPos
        // As per numpad, except the gap is on the top row...
        if (prevPos.y == 0 && nextPos.x == 0) {
            return "v".repeatSafe(delta.y.absoluteValue) + "<".repeatSafe(delta.x.absoluteValue)
        } else if (prevPos.x == 0 && nextPos.y == 0) {
            return ">".repeatSafe(delta.x.absoluteValue) + "^".repeatSafe(delta.y.absoluteValue)
        }
        return "<".repeatSafe(-delta.x) + "v".repeatSafe(delta.y) + "^".repeatSafe(-delta.y) + ">".repeatSafe(delta.x)
    }

    data class CacheKey(val from: Char, val dir: Char, val robots: Int)

    private val cache = mutableMapOf<CacheKey, Long>()

    private fun solveMemo(line: String, robots: Int): Long {
        val dirs = numPadToDirs(line)
        val result = "A${dirs}"
            .zipWithNext { l, r -> solveMemoInner(l, r, robots) }
            .sum()
        return line.dropLast(1).toInt() * result
    }

    private fun solveMemoInner(from: Char, dir: Char, robots: Int): Long {
        if (robots == 0) return 1
        val cacheKey = CacheKey(from, dir, robots)
        return cache.getOrPut(cacheKey) {
            val dirs = dirPath(from, dir)
            "A${dirs}A"
                .zipWithNext { l, r -> solveMemoInner(l, r, robots - 1) }
                .sum()
        }
    }

    override fun solvePart1(input: List<String>): Long {
        return input.sumOf { solveMemo(it, 2) }
    }

    override fun solvePart2(input: List<String>): Long {
        return input.sumOf { solveMemo(it, 25) }
    }

}

fun main() {
    Harness.run(Day21)
}
