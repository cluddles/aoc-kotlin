package aoc.y2024

import aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day05Test {

    val solver = Day05

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(143)
    }

    private fun testFixOrder(from: String, expected: String) {
        assertThat(solver.fixOrder(input.rules, solver.parseUpdate(from)))
            .isEqualTo(solver.parseUpdate(expected))
    }

    @Test fun fixOrder1() {
        testFixOrder("75,97,47,61,53", "97,75,47,61,53")
    }

    @Test fun fixOrder2() {
        testFixOrder("61,13,29", "61,29,13")
    }

    @Test fun fixOrder3() {
        testFixOrder("97,13,75,29,47", "97,75,47,29,13")
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(123)
    }

}
