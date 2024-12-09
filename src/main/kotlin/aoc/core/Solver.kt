package aoc.core

import kotlin.time.measureTime
import kotlin.time.measureTimedValue

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
        measureTime {
            val (input, t) = measureTimedValue { solver.prepareInput(path) }
            println("Loaded input in $t")

            println("\nPart 1:")
            measureTime {
                println(solver.solvePart1(input))
            }.also { println("Took $it") }

            println("\nPart 2:")
            measureTime {
                println(solver.solvePart2(input))
            }.also { println("Took $it") }

        }.also { println("\nTotal time elapsed: $it") }
    }

}
