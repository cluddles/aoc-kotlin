package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day12Test {

    val solver = Day12

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(1930)
    }

    @Test fun part2_simple() {
        val text = """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
        """.trimIndent()
        assertThat(solver.solvePart2(solver.prepareInput(SolverInput.fromText(text)))).isEqualTo(368)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(1206)
    }
}
