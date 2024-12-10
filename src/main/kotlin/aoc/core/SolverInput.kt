package aoc.core

interface SolverInput {

    fun text(): String
    fun lines(allowBlankLines: Boolean = false): Sequence<String> = Resource.splitIntoLines(text(), allowBlankLines)

    companion object {
        fun fromText(text: String): SolverInput = SolverInputFromText(text)
        fun fromPath(path: String): SolverInput = SolverInputFromPath(path)
    }

}

private class SolverInputFromText(val text: String) : SolverInput {
    override fun text() = text
}

private class SolverInputFromPath(val path: String) : SolverInput {
    override fun text() = Resource.loadText(path)
}
