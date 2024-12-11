package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day06Test {

    private val solution = Day06()

    @Test fun solvePart1_sampleInput1() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day06.sample.1"))).isEqualTo(6)
    }
    @Test fun solvePart1_sampleInput2() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day06.sample.2"))).isEqualTo(11)
    }

    @Test fun solvePart2_sampleInput2() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day06.sample.2"))).isEqualTo(6)
    }

}
