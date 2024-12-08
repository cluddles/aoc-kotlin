package aoc.y2024

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day07Test {

    val solver = Day07

    val input = solver.prepareInput("2024/tests/day07")

    private fun testEval(input: String, expected: Boolean, allowConcat: Boolean = false) {
        val eq = solver.parseEquation(input)
        assertThat(solver.eval(eq.answer, eq.values, allowConcat)).isEqualTo(expected)
    }

    @Test fun evalPart1_1() {
        testEval("190: 10 19", true)
    }
    @Test fun evalPart1_2() {
        testEval("3267: 81 40 27", true)
    }
    @Test fun evalPart1_3() {
        testEval("292: 11 6 16 20", true)
    }

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(3749L)
    }

    @Test fun evalPart2_1() {
        testEval("156: 15 6", true, allowConcat = true)
    }
    @Test fun evalPart2_2() {
        testEval("7290: 6 8 6 15", true, allowConcat = true)
    }
    @Test fun evalPart2_3() {
        testEval("192: 17 8 14", true, allowConcat = true)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(11387L)
    }

}
