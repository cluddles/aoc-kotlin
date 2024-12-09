package aoc.y2024

import aoc.core.Harness
import aoc.core.Resource
import aoc.core.Solver

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

    override fun prepareInput(path: String): String = Resource.asLines(path).first()

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
        ids@ for (fromId in disk.maxId downTo 0) {
            // Find the block to move
            val fromStart = disk.data.indexOf(fromId)
            var fromEnd = fromStart + 1
            while (fromEnd < disk.data.size && disk.data[fromEnd] == fromId) { fromEnd++ }
            // Find free block big enough
            var freeStart = -1
            var freeEnd = -1
            for (i in 0 until fromStart) {
                if (disk.data[i] == BLANK) {
                    if (freeStart == -1) freeStart = i
                    freeEnd = i+1
                    if (freeEnd - freeStart >= fromEnd - fromStart) {
                        // Move it and proceed to next id
                        for (j in fromStart until fromEnd) disk.data[j] = BLANK
                        for (j in freeStart until freeEnd) disk.data[j] = fromId
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
    Harness.run(Day09, "2024/day09")
}
