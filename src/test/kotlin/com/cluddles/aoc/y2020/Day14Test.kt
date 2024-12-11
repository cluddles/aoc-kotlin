package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day14Test {

    private val solution = Day14()

    @Test fun solvePart1_sampleInput1() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day14.sample.1"))).isEqualTo(165)
    }

    @Test fun solvePart2_sampleInput2() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day14.sample.2"))).isEqualTo(208)
    }

}
