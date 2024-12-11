package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput

/** Disk Fragmenter */
object Day09: Solver<String, Long> {

    const val BLANK = -1

    /**
     * Disk [data] (id per block) and the [maxId] used.
     *
     * Note that this is mutable, so not a good idea to use as an input.
     */
    class Disk(val data: IntArray, val maxId: Int) {
        // Note that this is not useful when IDs > 9
        override fun toString(): String {
            val result = StringBuffer(data.size)
            for (id in data) {
                result.append(if (id == BLANK) '.' else id.toString())
            }
            return result.toString()
        }
    }

    override fun prepareInput(src: SolverInput): String = src.lines().first()

    /** Convert [text] in input format (alternating file length, free space length) to [Disk] */
    fun createDiskFromInput(text: String): Disk {
        // Work out how much space we need
        val length = text.sumOf { it.digitToInt() }
        val result = IntArray(length) { BLANK }
        // Fill it - alternate between files (incrementing ID) and free space
        var freeSpace = false
        var id = 0
        var target = 0
        for (ch in text) {
            val length = ch.digitToInt()
            if (!freeSpace) {
                for (j in target until target + length) {
                    result[j] = id
                }
                id++
            }
            freeSpace = !freeSpace
            target += length
        }
        return Disk(result, id - 1)
    }

    /** Create [Disk] from text [blocks] (e.g. `0..111....22222`) */
    fun createDiskFromBlocks(blocks: String): Disk {
        val fixed = blocks.map { if (it == '.') BLANK else it.digitToInt() }
        return Disk(fixed.toIntArray(), fixed.max())
    }

    /** Calculate checksum: sum of `id` * `position` for all blocks */
    fun checksum(disk: Disk): Long {
        var sum = 0L
        for (i in disk.data.indices) {
            val id = disk.data[i]
            if (id != BLANK) sum += i * id
        }
        return sum
    }

    override fun solvePart1(input: String): Long {
        val disk = createDiskFromInput(input)
        // Track beginning and end so we don't have to recalculate from scratch every iteration
        var to = 0
        var from = disk.data.size - 1
        while (to < from) {
            if (disk.data[from] == BLANK) {
                from--
            } else if (disk.data[to] != BLANK) {
                to++
            } else {
                disk.data[to] = disk.data[from]
                disk.data[from] = BLANK
            }
        }
        return checksum(disk)
    }

    override fun solvePart2(input: String): Long {
        // It would probably be more efficient to have a data structure similar to the input
        // (list of files, where each file consists of id, length)
        val disk = createDiskFromInput(input)

        // Remember where we got to for previous free space scans
        val prevFree = IntArray(10)
        // Also remember where we got to when scanning backwards for blocks to move
        var prevFrom = disk.data.size

        ids@ for (fromId in disk.maxId downTo 0) {
            // Find the block to move
            var fromEnd = prevFrom
            while (fromEnd > 0 && disk.data[fromEnd - 1] != fromId) { fromEnd-- }
            var fromStart = fromEnd - 1
            while (fromStart > 0 && disk.data[fromStart - 1] == fromId) { fromStart-- }
            val size = fromEnd - fromStart

            // Find chunk of free space that's big enough
            var freeStart = -1
            var freeEnd = -1
            for (i in prevFree[size] until fromStart) {
                if (disk.data[i] == BLANK) {
                    if (freeStart == -1) freeStart = i
                    freeEnd = i+1
                    if (freeEnd - freeStart >= size) {
                        // Move the blocks
                        for (j in 0 until size) {
                            disk.data[fromStart + j] = BLANK
                            disk.data[freeStart + j] = fromId
                        }
                        // Remember scan positions
                        prevFree[size] = freeEnd
                        prevFrom = fromStart
                        continue@ids
                    }
                } else {
                    freeStart = -1
                }
            }
        }
        return checksum(disk)
    }

}

fun main() {
    Harness.run(Day09)
}
