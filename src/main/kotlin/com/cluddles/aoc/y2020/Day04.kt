package com.cluddles.aoc.y2020

import java.io.File

class Day04 {

    private val requiredFields = listOf("byr","iyr","eyr","hgt","hcl","ecl","pid")

    private val eyeColours = hashSetOf("amb","blu","brn","gry","grn","hzl","oth")
    private val hairColourRegex = Regex("#[0-9a-f]{6}")

    class Passport {
        val fields: HashMap<String, String> = HashMap()
    }

    private fun parseInput(lines: List<String>) : List<Passport> {
        val result = ArrayList<Passport>()
        var passport = Passport()
        for (line in lines) {
            if (line.isBlank()) {
                result.add(passport)
                passport = Passport()
            } else {
                for (kvp in line.split(" ")) {
                    val split = kvp.split(":")
                    passport.fields[split[0]] = split[1]
                }
            }
        }
        result.add(passport)
        return result
    }

    private fun isPassportDataPresent(passport: Passport) : Boolean {
        return passport.fields.keys.containsAll(requiredFields)
    }

    private fun isPassportDataValid(passport: Passport) : Boolean {
        return !passport.fields.entries.any{ !isPassportFieldValid(it.key, it.value) }
    }

    private fun isPassportFieldValid(key: String, value: String) : Boolean {
        /*
            byr (Birth Year) - four digits; at least 1920 and at most 2002.
            iyr (Issue Year) - four digits; at least 2010 and at most 2020.
            eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
            hgt (Height) - a number followed by either cm or in:
                If cm, the number must be at least 150 and at most 193.
                If in, the number must be at least 59 and at most 76.
            hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
            ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
            pid (Passport ID) - a nine-digit number, including leading zeroes.
            cid (Country ID) - ignored, missing or not.
         */
        return when(key) {
            "byr" -> value.toIntOrNull() in 1920..2002
            "iyr" -> value.toIntOrNull() in 2010..2020
            "eyr" -> value.toIntOrNull() in 2020..2030
            "hgt" -> isValidHeight(value)
            "hcl" -> hairColourRegex.matches(value)
            "ecl" -> value in eyeColours
            "pid" -> value.length == 9 && value.toIntOrNull() != null
            else -> true
        }
    }

    private fun isValidHeight(value: String) : Boolean {
        val num = value.substring(0, value.length - 2)
        return if (value.endsWith("cm")) num.toIntOrNull() in 150..193 else num.toIntOrNull() in 59..76
    }

    fun solvePart1(src: File) : Int {
        return parseInput(src.readLines())
            .filter { isPassportDataPresent(it) }
            .count()
    }

    fun solvePart2(src: File) : Int {
        return parseInput(src.readLines())
            .filter { isPassportDataPresent(it) }
            .filter { isPassportDataValid(it) }
            .count()
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day04()
            val file = File("aoc-secret/2020/day04")
            println(solver.solvePart1(file))
            println(solver.solvePart2(file))
        }
    }

}
