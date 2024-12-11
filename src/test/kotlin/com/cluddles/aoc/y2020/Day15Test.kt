package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day15Test {

    private val solution = Day15()

    @Test fun solvePart1_sampleInput1() {
        assertThat(solution.solvePart1("0,3,6")).isEqualTo(436)
    }
    @Test fun solvePart1_sampleInput2() {
        assertThat(solution.solvePart1("1,3,2")).isEqualTo(1)
    }
    @Test fun solvePart1_sampleInput3() {
        assertThat(solution.solvePart1("2,1,3")).isEqualTo(10)
    }
    @Test fun solvePart1_sampleInput4() {
        assertThat(solution.solvePart1("1,2,3")).isEqualTo(27)
    }
    @Test fun solvePart1_sampleInput5() {
        assertThat(solution.solvePart1("2,3,1")).isEqualTo(78)
    }
    @Test fun solvePart1_sampleInput6() {
        assertThat(solution.solvePart1("3,2,1")).isEqualTo(438)
    }
    @Test fun solvePart1_sampleInput7() {
        assertThat(solution.solvePart1("3,1,2")).isEqualTo(1836)
    }

    @Test fun solvePart2_sampleInput1() {
        assertThat(solution.solvePart2("0,3,6")).isEqualTo(175594)
    }
    @Test fun solvePart2_sampleInput2() {
        assertThat(solution.solvePart2("1,3,2")).isEqualTo(2578)
    }
    @Test fun solvePart2_sampleInput3() {
        assertThat(solution.solvePart2("2,1,3")).isEqualTo(3544142)
    }
    @Test fun solvePart2_sampleInput4() {
        assertThat(solution.solvePart2("1,2,3")).isEqualTo(261214)
    }
    @Test fun solvePart2_sampleInput5() {
        assertThat(solution.solvePart2("2,3,1")).isEqualTo(6895259)
    }
    @Test fun solvePart2_sampleInput6() {
        assertThat(solution.solvePart2("3,2,1")).isEqualTo(18)
    }
    @Test fun solvePart2_sampleInput7() {
        assertThat(solution.solvePart2("3,1,2")).isEqualTo(362)
    }

}
