package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day13Test {

    val solver = Day13

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(480)
    }

    @Test fun part2() {
        // Part two only tells us that the 2nd and 4th claw games are winnable
        val score = { s: Day13.Scenario -> solver.score(s, solver.PART2_OFFSET) }
        assertThat(score(input[0])).isEqualTo(-1L)
        assertThat(score(input[1])).isNotEqualTo(-1L)
        assertThat(score(input[2])).isEqualTo(-1L)
        assertThat(score(input[3])).isNotEqualTo(-1L)
    }

}
