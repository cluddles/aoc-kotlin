package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day05Test {

    private val solution = Day05()

    @Test fun solvePart1_sampleInput1() {
        assertThat(solution.seatId("FBFBBFFRLR")).isEqualTo(357)
    }
    @Test fun solvePart1_sampleInput2() {
        assertThat(solution.seatId("BFFFBBFRRR")).isEqualTo(567)
    }
    @Test fun solvePart1_sampleInput3() {
        assertThat(solution.seatId("FFFBBBFRRR")).isEqualTo(119)
    }
    @Test fun solvePart1_sampleInput4() {
        assertThat(solution.seatId("BBFFBBFRLL")).isEqualTo(820)
    }

}
