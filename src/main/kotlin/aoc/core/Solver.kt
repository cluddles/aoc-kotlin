package aoc.core

interface Solver<I, O> {

    /** Load input from [path] */
    fun prepareInput(path: String): I

    /** Solve part 1 for given [input] */
    fun solvePart1(input: I): O
    /** Solve part 2 for given [input] */
    fun solvePart2(input: I): O

}

object Harness {

    /** Run [solver] for both parts of a day's puzzle, using input loaded from [path] */
    fun <I, O> run(solver: Solver<I, O>, path: String) {
        val input = solver.prepareInput(path)
        println(solver.solvePart1(input))
        println(solver.solvePart2(input))
    }

}
