package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Int2d
import com.cluddles.aoc.y2024.Day21.dirPadKeys
import com.cluddles.aoc.y2024.Day21.numPadKeys
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
    private fun convertNumPadLineToDirInput(seq: String, startFrom: Char = 'A'): String {
        return seq.indices
            .joinToString("A") { i -> convertNumPadStep(if (i == 0) startFrom else seq[i - 1], seq[i]) } + "A"
    }

    private fun convertNumPadStep(prevChar: Char, nextChar: Char): String {
        val prevPos = numPadKeys[prevChar] ?: error("Bad key: $prevChar")
        val nextPos = numPadKeys[nextChar] ?: error("Bad key: $nextChar")
        // Exception: if starting from bottom row, you cannot go left over the "gap"
        val delta = nextPos - prevPos
        if (prevPos.y == 3 && nextPos.x == 0) {
            return "^".repeatSafe(delta.y.absoluteValue) + "<".repeatSafe(delta.x.absoluteValue)
        } else if (prevPos.x == 0 && nextPos.y == 3) {
            return ">".repeatSafe(delta.x.absoluteValue) + "v".repeatSafe(delta.y.absoluteValue)
        }
        return "<".repeatSafe(-delta.x) + "v".repeatSafe(delta.y) + "^".repeatSafe(-delta.y) + ">".repeatSafe(delta.x)
    }

    /** For a directional pad sequence, convert to one shortest directional pad input for parent robot */
    private fun convertDirPadSequence(seq: String, startFrom: Char = 'A'): String {
        return seq.indices
            .joinToString("A") { i -> convertDirPadStep(if (i == 0) startFrom else seq[i - 1], seq[i]) } + "A"
    }

    private fun convertDirPadStep(prevChar: Char, nextChar: Char): String {
        val prevPos = dirPadKeys[prevChar] ?: error("Bad key: $prevChar")
        val nextPos = dirPadKeys[nextChar] ?: error("Bad key: $nextChar")
        val delta = nextPos - prevPos
        // As per numpad, except the gap is on the top row...
        if (prevPos.y == 0 && nextPos.x == 0) {
            return "v".repeatSafe(delta.y.absoluteValue) + "<".repeatSafe(delta.x.absoluteValue)
        } else if (prevPos.x == 0 && nextPos.y == 0) {
            return ">".repeatSafe(delta.x.absoluteValue) + "^".repeatSafe(delta.y.absoluteValue)
        }
        return "<".repeatSafe(-delta.x) + "v".repeatSafe(delta.y) + "^".repeatSafe(-delta.y) + ">".repeatSafe(delta.x)
    }

    /**
     * Inflate numeric keypad strokes to list of directional keypad commands
     * This "works" up to about 19 robots or so (although it gets pretty slow)
     */
    private fun solveInflate(line: String, robots: Int): Int {
        var dirs = convertNumPadLineToDirInput(line)
        for (i in 0 until robots) {
            dirs = convertDirPadSequence(dirs, 'A')
        }
        val check = Day14Sim.eval(dirs, robots)
        require(line == check) { "Check failed: wanted '$line', got '$check'" }
        return line.dropLast(1).toInt() * dirs.length
    }

    data class CacheKey(val from: Char, val dir: Char, val robots: Int)

    private val cache = mutableMapOf<CacheKey, Long>()

    private fun solveMemo(line: String, robots: Int): Long {
        val dirs = convertNumPadLineToDirInput(line)
        val result = "A${dirs}"
            .zipWithNext { l, r -> solveMemoInner(l, r, robots) }
            .sum()
        return line.dropLast(1).toInt() * result
    }

    private fun solveMemoInner(from: Char, dir: Char, robots: Int): Long {
        if (robots == 0) return 1
        val cacheKey = CacheKey(from, dir, robots)
        return cache.getOrPut(cacheKey) {
            val dirs = convertDirPadStep(from, dir)
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

/** Quick debug to run generated commands and make sure they're valid (guess what - they weren't) */
object Day14Sim {

    interface Pad {
        var x: Int
        var y: Int
    }

    data class DirPad(override var x: Int = 2, override var y: Int = 0, val index: Int, val child: Pad) : Pad
    data class NumPad(override var x: Int = 2, override var y: Int = 3) : Pad

    fun eval(dirs: String, robots: Int): String {
        val depth = robots + 1
        var pads = mutableListOf<Pad>().apply {
            val numpad = NumPad()
            this += numpad
            var child: Pad = numpad
            repeat(depth - 1) {
                val kp = DirPad(index = it, child = child)
                this.add(0, kp)
                child = kp
            }
        }
        return dirs.map { evalOne(pads.first(), it) }.joinToString("")
    }

    private fun evalOne(pad: Pad, d: Char): String {
        if (d != 'A') {
            val dir = Dir4.fromChar(d)
            pad.x += dir.x
            pad.y += dir.y
            if (pad is DirPad && pad.x == 0 && pad.y == 0) {
                error("DirPad ${pad.index} invalid")
            }
            if (pad is NumPad && pad.x == 0 && pad.y == 3) {
                error("NumPad invalid")
            }

        } else {
            if (pad is DirPad) {
                val d2 = dirPadKeys.entries.first { it.value.x == pad.x && it.value.y == pad.y }.key
                return evalOne(pad.child, d2)

            } else {
                val d2 = numPadKeys.entries.first { it.value.x == pad.x && it.value.y == pad.y }.key
                return "$d2"
            }
        }
        return ""
    }
}

fun main() {
    Harness.run(Day21)
}
