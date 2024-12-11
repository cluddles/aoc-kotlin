package aoc.y2024

import aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day11Test {

    val solver = Day11

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    private fun testBlink(start: String, expected: String) {
        val input = solver.prepareInput(SolverInput.fromText(start))
        val result = solver.blink(input)
        assertThat(result.joinToString(" ")).isEqualTo(expected)
    }

    @Test fun blink1() {
        testBlink("0 1 10 99 999", "1 2024 1 0 9 9 2021976")
    }

    @Test fun blink2_1() {
        testBlink("125 17", "253000 1 7")
    }
    @Test fun blink2_2() {
        testBlink("253000 1 7", "253 0 2024 14168")
    }
    @Test fun blink2_3() {
        testBlink("253 0 2024 14168", "512072 1 20 24 28676032")
    }
    @Test fun blink2_4() {
        testBlink("512072 1 20 24 28676032", "512 72 2024 2 0 2 4 2867 6032")
    }
    @Test fun blink2_5() {
        testBlink("512 72 2024 2 0 2 4 2867 6032", "1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32")
    }
    @Test fun blink2_6() {
        testBlink(
            "1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32",
            "2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2"
        )
    }

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(55312)
    }

    // There is no given output for part 2

}
