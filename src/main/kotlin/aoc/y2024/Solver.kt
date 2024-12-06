package aoc.y2024

interface Solver<I, O> {

    fun prepareInput(path: String): I
    fun solvePart1(input: I): O
    fun solvePart2(input: I): O

}

object Harness {

    fun <I, O> run(solver: Solver<I, O>, path: String) {
        val input = solver.prepareInput(path)
        println(solver.solvePart1(input))
        println(solver.solvePart2(input))
    }

}
