package aoc.y2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day04Test {

    val solver = Day04

    val input = solver.prepareInput("2024/tests/day04")

    @Test fun examplePart1() {
        assertThat(solver.solvePart1(input)).isEqualTo(18)
    }

    @Test fun examplePart2() {
        assertThat(solver.solvePart2(input)).isEqualTo(9)
    }

}
