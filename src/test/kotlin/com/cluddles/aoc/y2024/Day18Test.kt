package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day18Test {

    val solver = Day18

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath), 7, 7)

    @Test fun part1() {
        assertThat(solver.solvePart1(input, 12)).isEqualTo(22)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo("6,1")
    }

}
