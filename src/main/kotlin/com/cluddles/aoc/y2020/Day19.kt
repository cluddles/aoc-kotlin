package com.cluddles.aoc.y2020

import java.io.File

// Don't forget that typealiases aren't local, so if you use the name "Rule" you're
// just going to have clashes later...
typealias Day19Grammar = Map<String, Day19Rule>
typealias Day19Rule = Set<String>

class Day19 {

    // Returns (mutable) grammar and list of input strings
    private fun parse(src: File) : Pair<MutableMap<String, Day19Rule>, List<String>> {
        val lines = src.readLines()
        val grammar = lines.filter { it.contains(": ") }
            .map {
                it.split(": ").let { parts ->
                    Pair(parts[0], parts[1].split(" | ").toSet())
                }
            }.toMap().toMutableMap()
        val input = lines.filter { it.isNotBlank() && !it.contains(": ") }
        return Pair(grammar, input)
    }

    // Basically the same as my first attempt, except now it returns all possible endpoints for the match
    // instead of just the one with the highest value
    private fun matchInner(grammar: Day19Grammar, rule: Day19Rule, expr: String, pos: Int) : List<Int> =
        when {
            pos >= expr.length -> emptyList()
            rule.size > 1 -> {
                rule.flatMap { matchInner(grammar, setOf(it), expr, pos) }
            }
            rule.first().startsWith("\"") -> {
                if (pos < expr.length && expr.substring(pos, pos + 1) == rule.first()[1].toString()) {
                    listOf(pos + 1)
                } else {
                    emptyList()
                }
            }
            else -> {
                // Test all potential match starts against each rule
                var pl = listOf(pos)
                for (r in rule.first().split(" ")) {
                    val pl2 = ArrayList<Int>()
                    for (p in pl) {
                        pl2.addAll(matchInner(grammar, grammar[r]!!, expr, p))
                    }
                    pl = pl2
                    if (pl.isEmpty()) break
                }
                pl
            }
        }

    private fun match(grammar: Day19Grammar, input: String) : Boolean =
        matchInner(grammar, grammar["0"]!!, input, 0).any { it == input.length }

    fun solvePart1(src: File) : Int {
        val (grammar, lines) = parse(src)
        return lines.count { match(grammar, it) }
    }

    fun solvePart2(src: File) : Int {
        val (grammar, lines) = parse(src)
        // Modify the grammar
        grammar["8"] = setOf("42", "42 8")
        grammar["11"] = setOf("42 31", "42 11 31")
        return lines.count { match(grammar, it) }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day19()
            val file = File("aoc-secret/2020/day19")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
