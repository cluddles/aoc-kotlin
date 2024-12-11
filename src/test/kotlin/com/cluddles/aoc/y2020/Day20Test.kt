package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day20Test {

    private val solution = Day20()

    @Test fun rotate() {
        val t = listOf(
            "#...",
            "##..",
            "#.#.",
            "..##"
        )
        assertThat(Day20.Composite.rotate(t)).isEqualTo(listOf(
            ".###",
            "..#.",
            "##..",
            "#..."))
    }

    @Test fun flipX() {
        val t = listOf(
            "#...",
            "##..",
            "#.#.",
            "..##"
        )
        assertThat(Day20.Composite.flipX(t)).isEqualTo(listOf(
            "...#",
            "..##",
            ".#.#",
            "##.."))
    }

    @Test fun solvePart1_sampleInput() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day20.sample"))).isEqualTo(20899048083289L)
    }

    @Test fun solvePart2_sampleInput() {
        assertThat(solution.solvePart2(File("src/test/resources/examples/2020/day20.sample"))).isEqualTo(273)
    }

}
