package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day18Test {

    private val solution = Day18()

    @Test fun solvePart1_sampleInput1() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day18.sample.1"))).isEqualTo(71)
    }
    @Test fun solvePart1_sampleInput2() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day18.sample.2"))).isEqualTo(51)
    }
    @Test fun solvePart1_sampleInput3() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day18.sample.3"))).isEqualTo(26+437+12240+13632)
    }

    @Test fun solvePart2_sampleInput1() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day18.sample.1"))).isEqualTo(231)
    }
    @Test fun solvePart2_sampleInput2() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day18.sample.2"))).isEqualTo(51)
    }
    @Test fun solvePart2_sampleInput3() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day18.sample.3"))).isEqualTo(46+1445+669060+23340)
    }

}
