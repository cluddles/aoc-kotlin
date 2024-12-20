package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.y2024.Day20.Cheat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day20Test {

    val solver = Day20

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun bestPathWithoutCheats() {
        assertThat(solver.generatePath(input).size).isEqualTo(84)
    }

    private fun countCheats(cheats: Map<Cheat, Int>, saving: Int): Int {
        return cheats.count { it.value == saving }
    }

    @Test fun part1() {
        val cheats = solver.uniqueCheats(input, 2, 0)
        assertThat(countCheats(cheats, 2)).isEqualTo(14)
        assertThat(countCheats(cheats, 4)).isEqualTo(14)
        assertThat(countCheats(cheats, 6)).isEqualTo(2)
        assertThat(countCheats(cheats, 8)).isEqualTo(4)
        assertThat(countCheats(cheats, 10)).isEqualTo(2)
        assertThat(countCheats(cheats, 12)).isEqualTo(3)
        assertThat(countCheats(cheats, 20)).isEqualTo(1)
        assertThat(countCheats(cheats, 36)).isEqualTo(1)
        assertThat(countCheats(cheats, 38)).isEqualTo(1)
        assertThat(countCheats(cheats, 40)).isEqualTo(1)
        assertThat(countCheats(cheats, 64)).isEqualTo(1)
        assertThat(cheats.size).isEqualTo(44)
    }

    @Test fun part2() {
        val cheats = solver.uniqueCheats(input, 20, 50)
        assertThat(countCheats(cheats, 50)).isEqualTo(32)
        assertThat(countCheats(cheats, 52)).isEqualTo(31)
        assertThat(countCheats(cheats, 54)).isEqualTo(29)
        assertThat(countCheats(cheats, 56)).isEqualTo(39)
        assertThat(countCheats(cheats, 58)).isEqualTo(25)
        assertThat(countCheats(cheats, 60)).isEqualTo(23)
        assertThat(countCheats(cheats, 62)).isEqualTo(20)
        assertThat(countCheats(cheats, 64)).isEqualTo(19)
        assertThat(countCheats(cheats, 66)).isEqualTo(12)
        assertThat(countCheats(cheats, 68)).isEqualTo(14)
        assertThat(countCheats(cheats, 70)).isEqualTo(12)
        assertThat(countCheats(cheats, 72)).isEqualTo(22)
        assertThat(countCheats(cheats, 74)).isEqualTo(4)
        assertThat(countCheats(cheats, 76)).isEqualTo(3)
        assertThat(cheats.size).isEqualTo(285)
    }

}
