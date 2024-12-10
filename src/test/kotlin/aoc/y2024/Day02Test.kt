package aoc.y2024

import aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day02Test {

    val solver = Day02

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(2)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(4)
    }

}
