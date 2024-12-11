package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day04Test {

    private val solution = Day04()

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day04.sample"))).isEqualTo(2)
    }

    @Test fun solvePart2_sampleInputInvalid() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day04.sample.invalid"))).isEqualTo(0)
    }

    @Test fun solvePart2_sampleInputValid() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day04.sample.valid"))).isEqualTo(4)
    }

}
