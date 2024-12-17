package com.cluddles.aoc.core

/** Puzzle input to be passed to [Solver] */
interface SolverInput {

    /** The input as a single String lump */
    fun text(): String
    /** The input split across multiple lines, with blank line behaviour dictated by [allowBlankLines] */
    fun lines(allowBlankLines: Boolean = false): Sequence<String> = Resource.splitIntoLines(text(), allowBlankLines)

    companion object {
        /** Puzzle input directly from [text] */
        fun fromText(text: String): SolverInput = SolverInputFromText(text)
        /** Puzzle input loaded from file */
        fun fromPath(path: String): SolverInput = SolverInputFromPath(path)
    }

}

private class SolverInputFromText(val text: String) : SolverInput {
    override fun text() = text
}

private class SolverInputFromPath(val path: String) : SolverInput {
    override fun text() = Resource.loadText(path)
}
