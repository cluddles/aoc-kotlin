package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day03Test {

    private val solution = Day03()

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day03.sample"))).isEqualTo(7)
    }

    @Test fun solvePart2_sampleInput() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day03.sample"))).isEqualTo(336)
    }

}
