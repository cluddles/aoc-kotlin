package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day19Test {

    private val solution = Day19()

    @Test fun solvePart1_sampleInput1() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day19.sample.1"))).isEqualTo(2)
    }
    @Test fun solvePart1_sampleInput2() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day19.sample.2"))).isEqualTo(3)
    }

    @Test fun solvePart2_sampleInput1() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day19.sample.2"))).isEqualTo(12)
    }

}
