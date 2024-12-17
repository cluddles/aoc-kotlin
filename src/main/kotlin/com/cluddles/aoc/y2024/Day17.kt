package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import kotlin.math.pow

/** Chronospatial Computer */
object Day17: Solver<Day17.Input, String> {

    data class Input(val a: Long, val b: Long, val c: Long, val program: List<Int>)

    override fun prepareInput(src: SolverInput): Input {
        val reg = src.lines().take(3).map { it.split(": ")[1].toLong() }.toList()
        return Input(
            reg[0],
            reg[1],
            reg[2],
            src.lines()
                .dropWhile { !it.contains("Program") }
                .first()
                .split(": ")[1]
                .split(",")
                .map { it.toInt() }
        )
    }

    data class State(var a: Long, var b: Long, var c: Long, var ip: Int, val out: MutableList<Int>)

    private enum class Instruction { ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV }

    private fun apply(state: State, i: Instruction, operand: Int) {
        when (i) {
            Instruction.ADV -> state.a = state.a / (2.0).pow(combo(state, operand).toDouble()).toLong()
            Instruction.BXL -> state.b = state.b xor operand.toLong()
            Instruction.BST -> state.b = combo(state, operand) % 8
            Instruction.JNZ -> if (state.a != 0L) state.ip = operand - 2
            Instruction.BXC -> state.b = state.b xor state.c
            Instruction.OUT -> state.out += (combo(state, operand) % 8).toInt()
            Instruction.BDV -> state.b = state.a / (2.0).pow(combo(state, operand).toDouble()).toLong()
            Instruction.CDV -> state.c = state.a / (2.0).pow(combo(state, operand).toDouble()).toLong()
        }
        // Move pointer along two (instruction + operand). Note JNZ is offset by -2 to counter this
        state.ip += 2
    }

    private fun combo(state: State, operand: Int): Long {
        return when(operand) {
            0, 1, 2, 3 -> operand.toLong()
            4 -> state.a
            5 -> state.b
            6 -> state.c
            else -> error("Unsupported: $operand")
        }
    }

    /** Run the given [input] program (with initial register values) and return final [State] */
    fun run(input: Input): State {
        val state = State(input.a, input.b, input.c, 0, mutableListOf())
        while (state.ip < input.program.size) {
            apply(state, Instruction.entries[input.program[state.ip]], input.program[state.ip + 1])
        }
        return state
    }

    override fun solvePart1(input: Input): String {
        return run(input).out.joinToString(",")
    }

    override fun solvePart2(input: Input): String {
        // For my input (And possibly others? It works on the test input too)
        // Each iteration around the program reduces the "a" register by dividing it by 8
        // So we can try to find a match for the last digit, then multiply by 8 and try for the next digit, and so on
        // Doing this with string.endsWith is probably not optimal, but it is concise...
        var a = 0L
        val inputProgStr = input.program.joinToString()
        do {
            val stateOutStr = run(Input(a, input.b, input.c, input.program)).out.joinToString()
            if (stateOutStr == inputProgStr) {
                return a.toString()
            } else if (inputProgStr.endsWith(stateOutStr)) {
                // 0*8 = 0... try not to get stuck
                a = maxOf(a * 8, 1)
            } else {
                a++
            }
        } while (stateOutStr.length <= inputProgStr.length)
        error("No solution")
    }

}

fun main() {
    Harness.run(Day17)
}
