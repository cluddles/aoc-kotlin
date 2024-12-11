package com.cluddles.aoc.y2020

import java.io.File

class Day21 {

    data class Food(val ingredients: List<String>, val allergens: List<String>)

    private fun parseFood(line: String) =
        line.split(" (contains ").let { Food(it[0].split(" "), it[1].dropLast(1).split(", ")) }

    private fun intersect(l1: List<String>, l2: List<String>?) : List<String> = l1.filter { l2?.contains(it) ?: true }

    private fun applyFoodToPossibilities(food: Food, possibilities: HashMap<String, List<String>>) {
        food.allergens.forEach { possibilities[it] = intersect(food.ingredients, possibilities[it]) }
    }

    private fun mapAllergenToIngredient(src: File) : Pair<List<Food>, Map<String, String>> {
        // Parse all the foods
        val foods = src.readLines().map { parseFood(it) }
        // Determine all possible allergen -> ingredient mappings by intersecting food list values
        val possibilities = HashMap<String, List<String>>()
        foods.forEach { food -> applyFoodToPossibilities(food, possibilities) }
        // Build up map of known allergen -> ingredient mappings
        // Remove possibilities as we go, which should reveal more known mappings
        val known = HashMap<String, String>()
        while (known.size < possibilities.size) {
            val ingredientsToRemove = HashSet<String>()
            possibilities.entries
                .filter { it.value.size == 1 && !known.contains(it.key) }
                .forEach {
                    known[it.key] = it.value.first()
                    ingredientsToRemove.add(it.value.first())
                }
            possibilities.keys
                .forEach { possibilities[it] = possibilities[it]!!.filter { ing -> !ingredientsToRemove.contains(ing) } }
        }
        return Pair(foods, known)
    }

    fun solvePart1(src: File) : Int {
        val (foods, allergenMap) = mapAllergenToIngredient(src)
        return foods.sumOf { f -> f.ingredients.count { ing -> !allergenMap.values.contains(ing) } }
    }

    fun solvePart2(src: File) : String {
        val (_, allergenMap) = mapAllergenToIngredient(src)
        return allergenMap.entries.sortedBy { it.key }.joinToString(",") { it.value }
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day21()
            val file = File("aoc-secret/2020/day21")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
