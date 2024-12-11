package com.cluddles.aoc.y2020

import java.io.File

class Day16 {

    data class Rule(val name: String, val ranges: List<IntRange>) {
        val possibleIndexes: HashSet<Int> = HashSet()
    }
    data class Ticket(val values: List<Int>)
    data class Notes(val rules: List<Rule>, val tickets: List<Ticket>)

    // Rule format = rulename: i1-i2 or j1-j2
    private fun parseRule(line: String) = line.split(": ").let { parts -> Rule(
        parts[0],
        parts[1].split(" or ").map { range ->
            range.split("-").let { IntRange(it[0].toInt(), it[1].toInt()) }
        }
    )}

    // Ticket format = x,y,z,...
    private fun parseTicket(line: String) = Ticket(line.split(",").map { it.toInt() } )

    private fun parseNotes(src: File) : Notes {
        val rules = ArrayList<Rule>()
        val tickets = ArrayList<Ticket>()
        src.forEachLine { line ->
            when {
                line.contains("-") -> rules.add(parseRule(line))
                line.contains(",") -> tickets.add(parseTicket(line))
                else -> {}
            }
        }
        return Notes(rules, tickets)
    }

    private fun ruleAccepts(rule: Rule, value: Int) : Boolean = rule.ranges.any { it.contains(value) }

    fun solvePart1(src: File) : Int {
        val notes = parseNotes(src)
        // Sum of all invalid values (those that can't match any rule)
        return notes.tickets.drop(1).flatMap { it.values }
            .map { value -> if (notes.rules.none { rule -> ruleAccepts(rule, value) } ) value else 0 }
            .sum()
    }

    fun parseNotesAndIndex(src: File) : Notes {
        val notes = parseNotes(src)
        // Phase 1 - strip invalid tickets
        val validTickets = notes.tickets.filter { ticket ->
            ticket.values.all { value ->
                notes.rules.any { rule -> ruleAccepts(rule, value) }
            }
        }
        // Phase 2 - use remaining (valid) tickets to determine possible indexes for each rule
        notes.rules.forEach { rule -> rule.possibleIndexes.addAll(notes.tickets[0].values.indices) }
        validTickets.forEach { ticket ->
            ticket.values.forEachIndexed { i, value ->
                notes.rules
                    .filter { rule -> rule.possibleIndexes.contains(i) && !ruleAccepts(rule, value) }
                    .forEach { rule -> rule.possibleIndexes.remove(i) }
            }
        }
        // Phase 3 - reduce rule index mappings based on other rules needing the same index
        val processed = HashSet<String>()
        do {
            val toProcess = notes.rules.filter { !processed.contains(it.name) && it.possibleIndexes.size <= 1 }
            if (toProcess.isEmpty()) throw IllegalStateException("Could not reduce rule index mappings")
            toProcess.forEach { procRule ->
                notes.rules.filter { it != procRule }.forEach { it.possibleIndexes.removeAll(procRule.possibleIndexes) }
            }
            processed.addAll(toProcess.map { it.name })
        } while (processed.size != notes.rules.size)
        return notes;
    }

    fun solvePart2(src: File) : Long {
        val notes = parseNotesAndIndex(src)
        // Multiply all values for rules with name starting "departure"
        return notes.rules
            .filter { it.name.startsWith("departure") }
            .map { notes.tickets.first().values[it.possibleIndexes.first()] }
            .fold(1L, { a, b -> a * b } )
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day16()
            val file = File("aoc-secret/2020/day16")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
