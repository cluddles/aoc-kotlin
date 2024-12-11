package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day21Test {

    private val solution = Day21()

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day21.sample"))).isEqualTo(5)
    }

    @Test fun solvePart2_sampleInput() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day21.sample"))).isEqualTo("mxmxvkd,sqjhc,fvjkl")
    }

}
