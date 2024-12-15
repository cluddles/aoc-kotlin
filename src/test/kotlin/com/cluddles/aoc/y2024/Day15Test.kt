package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day15Test {

    val solver = Day15

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1_simple() {
        val text = """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            <^^>>>vv<v>>v<<
        """.trimIndent()
        assertThat(solver.solvePart1(solver.prepareInput(SolverInput.fromText(text)))).isEqualTo(2028)
    }

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(10092)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(9021)
    }
}
