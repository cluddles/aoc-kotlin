package com.cluddles.aoc.y2020

import java.io.File
import java.util.*

class Day22 {

    data class Deck(val cards: Deque<Int>)
    data class Round(val decks: List<Deck>)
    data class GameResult(val winner: Int, val deck: Deck)

    private fun parseDeck(lines: List<String>) : Deck = Deck(LinkedList(lines.map { it.toInt() }))
    private fun parseAllDecks(src: File) : List<Deck> {
        val lines = src.readLines()
        val blankLine = lines.indexOfFirst { it.isBlank() }
        return listOf(
            parseDeck(lines.slice(1 until blankLine)),
            parseDeck(lines.slice(blankLine+2 until lines.size))
        )
    }

    // Decks are mutable, so we have need of creating copies for remembering previous state
    private fun copyDeck(deck: Deck) = Deck(LinkedList(deck.cards))
    // Commit deck state to a round that can be stored in memory
    private fun createRound(decks: List<Deck>) = Round(decks.map { copyDeck(it) })

    // Should we play a subgame with the given state?
    // If both players have >= as many cards remaining in their deck as the value of the card they just drew
    private fun canRecurse(drawn: List<Pair<Int, Int>>, decks: List<Deck>) =
        drawn.all { decks[it.first].cards.size >= it.second }
    // Play a subgame of Recursive Combat
    private fun recurse(drawn: List<Pair<Int, Int>>, decks: List<Deck>) = playCombat(
        drawn.sortedBy { it.first }.map { Deck(LinkedList(decks[it.first].cards.take(it.second))) },
        recursive = true
    )

    // Play a game of Combat, or a game/subgame of Recursive Combat
    // Modifies the given decks, so copy them before calling this if you want to preserve their state
    // Returns winning player index and the winning deck
    private fun playCombat(
        decks: List<Deck>,
        recursive: Boolean = false
    ) : GameResult {
        val memory = HashSet<Round>()
        while ( decks.none { it.cards.isEmpty() }) {
            // Recursive Combat - player 1 wins if we've seen this round state before
            if (!memory.add(createRound(decks))) return GameResult(0, decks[0])
            // Pop top card from each deck (with deck index) and order highest first
            val drawn = decks
                .mapIndexed { i, deck -> Pair(i, deck.cards.pop()) }
                .sortedByDescending { it.second }
            // Recursive Combat - play sub-game if both players have enough cards remaining in their deck to draw cards
            // equal to the value of the top card they just drew
            val winner = if (recursive && canRecurse(drawn, decks)) recurse(drawn, decks).winner else drawn[0].first
            // Make sure the winner's card is the one on top (only an issue for playing subgames)
            decks[winner].cards.addAll(drawn.filter { it.first == winner }.map { it.second })
            decks[winner].cards.addAll(drawn.filter { it.first != winner }.map { it.second })
        }
        val winner = decks.indexOfFirst { it.cards.isNotEmpty() }
        return GameResult(winner, decks[winner])
    }

    // (last card in deck * 1) + (2nd last card * 2) + ...
    private fun score(deck: Deck) = deck.cards.mapIndexed { i, card -> card * (deck.cards.size - i) }.sum()

    fun solvePart1(src: File) : Int = score(playCombat(parseAllDecks(src)).deck)
    fun solvePart2(src: File) : Int = score(playCombat(parseAllDecks(src), recursive = true).deck)

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day22()
            val file = File("aoc-secret/2020/day22")
            println("Part1: " + solver.solvePart1(file))
            println("Part2: " + solver.solvePart2(file))
        }
    }

}
