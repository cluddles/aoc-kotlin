package aoc.core

import kotlin.time.measureTime
import kotlin.time.measureTimedValue

object Harness {

    /** Run [solver] for both parts of a day's puzzle, using input loaded from [src] */
    fun <I, O> run(solver: Solver<I, O>, src: SolverInput? = null) {

        // Auto-determine path so I don't have to keep updating it (or forgetting)
        val src = src ?: SolverInput.fromPath(solver.inputPath)

        measureTime {
            println("\n== ${solver.javaClass.simpleName} ==")

            val (input, t) = measureTimedValue { solver.prepareInput(src) }
            println("\nLoaded input in $t")

            println("\nPart 1:")
            measureTime {
                println(solver.solvePart1(input))
            }.also { println("Took $it") }

            println("\nPart 2:")
            measureTime {
                println(solver.solvePart2(input))
            }.also { println("Took $it") }

        }.also { println("\nTotal elapsed time for this solution: $it") }
    }

}
