package com.cluddles.aoc.y2020

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day16Test {

    private val solution = Day16()

    @Test fun solvePart1_sampleInput1() {
        assertThat(solution.solvePart1(File("src/test/resources/examples/2020/day16.sample.1"))).isEqualTo(71)
    }

    @Test fun solvePart2_sampleInput2() {
        val notes = solution.parseNotesAndIndex(File("src/test/resources/examples/2020/day16.sample.2"))
        val namedRules = notes.rules.map { rule -> Pair(rule.name, rule) }.toMap()
        assertThat(namedRules).containsOnlyKeys("class", "row", "seat")
        assertThat(namedRules["class"]!!.possibleIndexes).containsOnly(1)
        assertThat(namedRules["row"]!!.possibleIndexes).containsOnly(0)
        assertThat(namedRules["seat"]!!.possibleIndexes).containsOnly(2)
    }

}
