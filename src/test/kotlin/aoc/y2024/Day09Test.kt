package aoc.y2024

import aoc.core.SolverInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day09Test {

    val solver = Day09

    val input = solver.prepareInput(SolverInput.fromPath(solver.examplePath))

    @Test fun createDiskFromInput() {
        assertThat(solver.createDiskFromInput("12345").toString()).isEqualTo("0..111....22222")
    }

    @Test fun checkdisk() {
        // Guess what I had a bug in
        assertThat(solver.checksum(solver.createDiskFromBlocks("00992111777.44.333....5555.6666.....8888..")))
            .isEqualTo(2858L)
    }

    @Test fun part1() {
        assertThat(solver.solvePart1(input)).isEqualTo(1928L)
    }

    @Test fun part2() {
        assertThat(solver.solvePart2(input)).isEqualTo(2858L)
    }

}
