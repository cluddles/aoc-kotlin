package aoc.y2024

import aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day07Test {

    val solver = Day07

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    private fun testEval(input: String, ops: List<Day07.Op>) {
        with (solver.parseEquation(input)) {
            assertThat(solver.eval(answer, values, ops)).isTrue()
            assertThat(solver.evalInverse(answer, values, ops)).isTrue()
        }
    }

    @Test fun evalPart1_1() {
        testEval("190: 10 19", Day07.opsPart1)
    }
    @Test fun evalPart1_2() {
        testEval("3267: 81 40 27", Day07.opsPart1)
    }
    @Test fun evalPart1_3() {
        testEval("292: 11 6 16 20", Day07.opsPart1)
    }

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(3749L)
    }

    @Test fun evalPart2_1() {
        testEval("156: 15 6", Day07.opsPart2)
    }
    @Test fun evalPart2_2() {
        testEval("7290: 6 8 6 15", Day07.opsPart2)
    }
    @Test fun evalPart2_3() {
        testEval("192: 17 8 14", Day07.opsPart2)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(11387L)
    }

}
