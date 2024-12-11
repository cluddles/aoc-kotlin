package com.cluddles.aoc.core

/**
 * @property I Solution input type
 * @property O Solution output type
 */
interface Solver<I, O> {

    /** Convert input from [src] into suitable format for solutions */
    fun prepareInput(src: SolverInput): I

    /** Solve part 1 for given [input] */
    fun solvePart1(input: I): O
    /** Solve part 2 for given [input] */
    fun solvePart2(input: I): O

    /** Where the solution input is located */
    val inputPath: String
        get() {
            return with(javaClass) {
                // package name: the 4 digits at the end are the year
                // class name: the 2 digits at the end are the day
                "${packageName.takeLast(4)}/day${simpleName.takeLast(2)}"
            }
        }

    /** Where the example input is located (for unit tests) */
    val examplePath: String
        get() = "examples/$inputPath"

}
