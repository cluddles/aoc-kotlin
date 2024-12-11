package com.cluddles.aoc.y2020

import java.io.File

class Day08 {

    private data class Instruction(val op: String, val arg: Int)

    private data class ExecResult(val stuck: Boolean, val acc: Int)

    private fun parse(src: File) : ArrayList<Instruction> {
        val result = ArrayList<Instruction>()
        src.forEachLine { line ->
            val parts = line.split(" ")
            result.add(Instruction(parts[0], parts[1].toInt()))
        }
        return result
    }

    private fun exec(prog: List<Instruction>) : ExecResult {
        var pos = 0
        var acc = 0
        var stuck = false
        val visited = HashSet<Int>()
        while (pos < prog.size) {
            if (visited.contains(pos)) { stuck = true; break }
            visited.add(pos)
            // Increment pos by default; amend jmp accordingly
            val current = prog[pos++]
            when (current.op) {
                "acc" -> acc += current.arg
                "jmp" -> pos += current.arg - 1
                else -> {}
            }
        }
        return ExecResult(stuck, acc)
    }

    // Flip a single nop/jmp in the program; returns false if nothing done
    private fun flipNopJmp(prog: ArrayList<Instruction>, pos: Int) : Boolean {
        val ins = prog[pos]
        when (ins.op) {
            "jmp" -> prog[pos] = Instruction("nop", ins.arg)
            "nop" -> prog[pos] = Instruction("jmp", ins.arg)
            else -> return false
        }
        return true
    }

    fun solvePart1(src: File) : Int {
        return exec(parse(src)).acc
    }

    fun solvePart2(src: File) : Int {
        val prog = parse(src)
        // Maybe a cleverer way of doing this if you can create an iterator that steps through prog permutations
        // Seems really longwinded though
        for (i in prog.indices) {
            if (flipNopJmp(prog, i)) {
                val result = exec(prog)
                if (!result.stuck) return result.acc
                flipNopJmp(prog, i)
            }
        }
        throw IllegalStateException("No solution")
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day08()
            val file = File("aoc-secret/2020/day08")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
