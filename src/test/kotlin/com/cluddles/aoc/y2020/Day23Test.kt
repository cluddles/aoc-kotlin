package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day23Test {

    private val solution = Day23()

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1("389125467")).isEqualTo("67384529")
    }

    @Test fun solvePart2_sampleInput() {
        assertThat(solution.solvePart2("389125467")).isEqualTo(149245887792L)
    }

}
