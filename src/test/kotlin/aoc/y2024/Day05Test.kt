package aoc.y2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day05Test {

    val solver = Day05

    val input = solver.prepareInput("2024/tests/day05")

    @Test fun examplePart1() {
        assertThat(solver.solvePart1(input)).isEqualTo(143)
    }

    @Test fun examplePart2() {
        assertThat(solver.solvePart2(input)).isEqualTo(123)
    }

}
