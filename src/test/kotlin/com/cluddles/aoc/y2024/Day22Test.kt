package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day22Test {

    val solver = Day22

    val input1 = solver.prepareInput(SolverInput.fromPath("${solver.examplePath}.1"))
    val input2 = solver.prepareInput(SolverInput.fromPath("${solver.examplePath}.2"))

    @Test fun part1() {
        assertThat(solver.solvePart1(input1)).isEqualTo(37327623)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input2)).isEqualTo(23)
    }
}
