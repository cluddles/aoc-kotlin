package aoc.y2024

import aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day01Test {

    val solver = Day01

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(11)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(31)
    }

}
