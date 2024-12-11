package com.cluddles.aoc.y2020

import java.io.File

class Day18 {

    // Inner-most parentheses, group 1 = the contents
    private val parenRegex = Regex("\\(([^()]*)\\)")

    // Remove parentheses by evaluating their contents
    private fun reduceParens(line: String, evalFunc: (a: String) -> Long) : String {
        var str = line
        var match: MatchResult? = parenRegex.find(str)
        while (match != null) {
            str = str.replaceRange(match.range, evalFunc(match.groupValues[1]).toString())
            match = parenRegex.find(str)
        }
        return str
    }

    // Evaluate simple a + b * c... expression
    private fun reduce(line: String, reduceOps: String = "+*") : String {
        var tokens = line.split(" ")
        var i = 0
        while (i + 2 < tokens.size) {
            if (reduceOps.contains(tokens[i+1])) {
                val reduced = when (tokens[i+1]) {
                    "+" -> tokens[i+0].toLong() + tokens[i+2].toLong()
                    "*" -> tokens[i+0].toLong() * tokens[i+2].toLong()
                    else -> throw IllegalStateException("Unhandled operator in: $line")
                }
                tokens = ArrayList<String>().let {
                    it.addAll(tokens.slice(0 until i))
                    it.add(reduced.toString())
                    it.addAll(tokens.slice(i + 3 until tokens.size))
                    it
                }
            } else {
                i += 2
            }
        }
        return tokens.joinToString(" ")
    }

    fun evalNoPrecedence(line: String) : Long = reduce(reduceParens(line, ::evalNoPrecedence)).toLong()
    fun eval(line: String) : Long = reduce(reduce(reduceParens(line, ::eval), "+"), "*").toLong()

    fun solvePart1(src: File) = src.readLines().map { evalNoPrecedence(it) }.sum()
    fun solvePart2(src: File) = src.readLines().map { eval(it) }.sum()

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day18()
            val file = File("aoc-secret/2020/day18")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
