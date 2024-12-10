package aoc.y2024

import aoc.core.SolverInput
import aoc.util.CharGrid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day08Test {

    val solver = Day08

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(14)
    }

    @Test fun simpleExamplePart2() {
        val text = """
            T....#....
            ...T......
            .T....#...
            .........#
            ..#.......
            ..........
            ...#......
            ..........
            ....#.....
            ..........
        """.trimIndent()
        assertThat(solver.solvePart2(solver.gridToInput(CharGrid(text.split("\n"))))).isEqualTo(9)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(34)
    }

}
