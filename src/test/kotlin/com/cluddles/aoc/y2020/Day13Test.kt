package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day13Test {

    private val solution = Day13()

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day13.sample"))).isEqualTo(295)
    }

    @Test fun solvePart2_sampleInput() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day13.sample"))).isEqualTo(1068781L)
    }

}
