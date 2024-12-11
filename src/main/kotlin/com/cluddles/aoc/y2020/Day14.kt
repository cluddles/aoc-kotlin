package com.cluddles.aoc.y2020

import java.io.File

class Day14 {

    // Instructions are either "mask = ..." or "mem[x] = y"
    // Addresses and values are 36-bit, so Int technically won't cut it
    // (although my part 1 was okay using Int for address, naughty naughty)
    interface Instruction
    data class Mask(val mask: String) : Instruction
    data class Mem(val address: Long, val value: Long) : Instruction

    private fun parse(src: File) : List<Instruction> = src.readLines().map { line ->
        when {
            line.startsWith("mask = ") -> Mask(line.drop(7))
            else -> Mem(
                line.split("[")[1].split("]")[0].toLong(),
                line.split(" = ")[1].toLong())
        }
    }

    fun solvePart1(src: File) : Long {
        // write to mem address, replace value bits with any non-X in mask
        val program = parse(src)
        val memory = HashMap<Long, Long>()
        var mask = "X".repeat(36)
        program.forEach { instr ->
            when (instr) {
                is Mask -> mask = instr.mask
                is Mem -> memory[instr.address] = instr.value
                    .toString(2)
                    .padStart(36, '0')
                    .mapIndexed { i, c -> if (mask[i] != 'X') mask[i] else c }
                    .joinToString("")
                    .toLong(2)
            }
        }
        return memory.values.sum()
    }

    fun solvePart2(src: File) : Long {
        // apply mask as OR to mem address; X values should map to all permutations of 0s and 1s
        val program = parse(src)
        val memory = HashMap<Long, Long>()
        var mask = "X".repeat(36)
        program.forEach { instr ->
            when (instr) {
                is Mask -> mask = instr.mask
                is Mem -> {
                    // 2 'X's - loop 4 times; 3 'X's - loop 8 times, etc
                    val numXBits = mask.count { it == 'X' }
                    for (perm in 0.."1".repeat(numXBits).toInt(2)) {
                        var xIndex = 0
                        val addr = instr.address
                            .toString(2)
                            .padStart(36, '0')
                            .mapIndexed { i, c ->
                                when (mask[i]) {
                                    'X' -> perm.toString(2).padStart(numXBits, '0')[xIndex++]
                                    '1' -> '1'
                                    else -> c
                                }
                            }
                            .joinToString("")
                            .toLong(2)
                        memory[addr] = instr.value
                    }
                }
            }
        }
        return memory.values.sum()
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day14()
            val file = File("aoc-secret/2020/day14")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
