package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day17Test {

    val solver = Day17

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    private fun testInput(program: String, a: Int = 0, b: Int = 0, c: Int = 0): Day17.Input {
        return Day17.Input(a.toLong(), b.toLong(), c.toLong(), program.split(",").map { it.toInt() })
    }

    @Test fun instructionOp_ex1() {
        assertThat(solver.run(testInput("2,6", c = 9)).b).isEqualTo(1)
    }

    @Test fun instructionOp_ex2() {
        assertThat(solver.run(testInput("5,0,5,1,5,4", a = 10)).out).isEqualTo(listOf(0,1,2))
    }

    @Test fun instructionOp_ex3() {
        val state = solver.run(testInput("0,1,5,4,3,0", a = 2024))
        assertThat(state.out).isEqualTo(listOf(4,2,5,6,7,7,7,7,3,1,0))
        assertThat(state.a).isEqualTo(0)
    }

    @Test fun instructionOp_ex4() {
        assertThat(solver.run(testInput("1,7", b = 29)).b).isEqualTo(26)
    }

    @Test fun instructionOp_ex5() {
        assertThat(solver.run(testInput("4,0", b = 2024, c = 43690)).b).isEqualTo(44354)
    }

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo("4,6,3,5,6,3,5,2,1,0")
    }

    @Test fun part2_check() {
        assertThat(solver.run(testInput("0,3,5,4,3,0", a = 117440)).out).isEqualTo(listOf(0,3,5,4,3,0))
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(testInput("0,3,5,4,3,0"))).isEqualTo("117440")
    }

}
