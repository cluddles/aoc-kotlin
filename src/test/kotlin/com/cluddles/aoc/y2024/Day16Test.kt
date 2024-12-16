package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day16Test {

    val solver = Day16

    val input1 = solver.prepareInput(SolverInput.fromPath("${solver.examplePath}.1"))
    val input2 = solver.prepareInput(SolverInput.fromPath("${solver.examplePath}.2"))

    @Test fun part1_example1() {
        assertThat(solver.solvePart1(input1)).isEqualTo(7036)
    }

    @Test fun part1_example2() {
        assertThat(solver.solvePart1(input2)).isEqualTo(11048)
    }

    @Test fun part2_example1() {
        assertThat(solver.solvePart2(input1)).isEqualTo(45)
    }

    @Test fun part2_example2() {
        assertThat(solver.solvePart2(input2)).isEqualTo(64)
    }

}
