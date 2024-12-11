package com.cluddles.aoc.y2020

import java.io.File

class Day07 {

    private class BagRule {
        val allowed: HashMap<String, Int> = HashMap()
        override fun toString(): String {
            return allowed.toString()
        }
    }

    private fun parse(src: File) : Map<String, BagRule> {
        val result = HashMap<String, BagRule>()
        src.forEachLine { line ->
            // X X bags contain Y X X bags, Y X X bags, Y X X bags.
            // X X bags contain no other bags.
            val rule = BagRule()
            val parts = line.split(" ")
            for (i in 4 until parts.size-3 step 4) {
                rule.allowed[parts[i+1] + " " + parts[i+2]] = parts[i].toInt()
            }
            result[parts[0] + " " + parts[1]] = rule
        }
        return result
    }

    private fun bagContains(rules: Map<String, BagRule>, name: String, target: String) : Boolean {
        return rules[name]!!.allowed.any { it.key == target || bagContains(rules, it.key, target) }
    }

    private fun countContents(rules: Map<String, BagRule>, name: String) : Int {
        // Don't count the top-most bag, but do count the child bags that contain other bags
        return rules[name]!!.allowed.entries.sumBy { (1 + countContents(rules, it.key)) * it.value }
    }

    fun solvePart1(src: File) : Int {
        val rules = parse(src)
        return rules.entries.count { bagContains(rules, it.key, "shiny gold") }
    }

    fun solvePart2(src: File) : Int {
        val rules = parse(src)
        return countContents(rules, "shiny gold")
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day07()
            val file = File("aoc-secret/2020/day07")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
