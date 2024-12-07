package aoc.y2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day03Test {

    val solver = Day03

    val input = solver.prepareInput("2024/tests/day03")

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(161)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(48)
    }

}
