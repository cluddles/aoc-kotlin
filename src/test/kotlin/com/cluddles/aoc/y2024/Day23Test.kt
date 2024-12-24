package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day23Test {

    val solver = Day23

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo("7")
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo("co,de,ka,ta")
    }
}
