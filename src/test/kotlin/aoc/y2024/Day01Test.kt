package aoc.y2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day01Test {

    val solver = Day01

    val input = solver.prepareInput("2024/tests/day01")

    @Test fun examplePart1() {
        assertThat(solver.solvePart1(input)).isEqualTo(11)
    }

    @Test fun examplePart2() {
        assertThat(solver.solvePart2(input)).isEqualTo(31)
    }

}
