package aoc.y2024

import aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day07Test {

    val solver = Day07

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    private fun testEval(input: String, expected: Boolean, ops: List<Day07.Op>) {
        with (solver.parseEquation(input)) {
            assertThat(solver.eval(answer, values, ops)).isEqualTo(expected)
        }
    }

    @Test fun evalPart1_1() {
        testEval("190: 10 19", true, Day07.opsPart1)
    }
    @Test fun evalPart1_2() {
        testEval("3267: 81 40 27", true, Day07.opsPart1)
    }
    @Test fun evalPart1_3() {
        testEval("292: 11 6 16 20", true, Day07.opsPart1)
    }

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(3749L)
    }

    @Test fun evalPart2_1() {
        testEval("156: 15 6", true, Day07.opsPart2)
    }
    @Test fun evalPart2_2() {
        testEval("7290: 6 8 6 15", true, Day07.opsPart2)
    }
    @Test fun evalPart2_3() {
        testEval("192: 17 8 14", true, Day07.opsPart2)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(11387L)
    }

}
