package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.Int2d
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day14Test {

    val solver = Day14

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1() {
        assertThat(solver.solvePart1(input, Int2d(11, 7))).isEqualTo(12)
    }

    // No test for part 2

}
