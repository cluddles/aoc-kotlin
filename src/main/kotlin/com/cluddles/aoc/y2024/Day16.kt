package com.cluddles.aoc.y2024

import com.cluddles.aoc.core.Harness
import com.cluddles.aoc.core.Solver
import com.cluddles.aoc.core.SolverInput
import com.cluddles.aoc.util.CharGrid
import com.cluddles.aoc.util.Dir4
import com.cluddles.aoc.util.Grid
import com.cluddles.aoc.util.Int2d
import kotlin.math.absoluteValue
import kotlin.math.sign

/** Reindeer Maze */
object Day16: Solver<Grid<Char>, Int> {

    const val WALL = '#'
    const val START = 'S'
    const val END = 'E'

    data class Node(val pos: Int2d, val dir: Dir4) {
        val x: Int; get() = pos.x
        val y: Int; get() = pos.y
    }

    override fun prepareInput(src: SolverInput): Grid<Char> {
        return CharGrid(src.lines().toList())
    }

    private fun h(node: Node, end: Int2d): Int {
        if (node.pos == end) return 0
        val turns: Int = if (end.x == node.x) {
            ((node.pos.y - end.y).sign).absoluteValue
        } else if (end.y == node.y) {
            ((node.pos.x - end.x).sign).absoluteValue
        } else {
            if ((node.x - end.x).sign != node.dir.x && (node.y - end.y).sign != node.dir.y) 2 else 1
        }
        return turns * 1000 + (end.x - node.pos.x).absoluteValue + (end.y - node.pos.y).absoluteValue
    }

    private fun generateNeighbours(node: Node, grid: Grid<Char>): List<Node> {
        // check 90 degree left, ahead, 90 degree right
        val result = mutableListOf<Node>()
        for (i in -1..1) {
            val dir = node.dir.rotate(i)
            val cell = grid[node.x + dir.x, node.y + dir.y]
            if (cell != WALL && cell != START) {
                result += Node(Int2d(node.x + dir.x, node.y + dir.y), dir)
            }
        }
        return result
    }

    private fun reconstructPath(cameFrom: Map<Node, Node>, node: Node): List<Node> {
        var node = node
        var prev = cameFrom[node]
        val result = mutableListOf<Node>()
        result += node
        while (prev != null) {
            result += prev
            node = prev
            prev = cameFrom[node]
        }
        return result
    }

    override fun solvePart1(input: Grid<Char>): Int {
        val start = input.iterableWithPos().firstOrNull { it.data == START }?.let { Node(Int2d(it.x, it.y), Dir4.E) }
            ?: error("No start")
        val end = input.iterableWithPos().firstOrNull { it.data == END }?.let { Int2d(it.x, it.y) }
            ?: error("No end")

        // omg it's a* again

        val cameFrom = mutableMapOf<Node, Node>()
        val gScore = mutableMapOf<Node, Int>()
        gScore[start] = 0
        val fScore = mutableMapOf<Node, Int>()

        val openSet = mutableSetOf(start)
        while (openSet.isNotEmpty()) {
            val current = openSet.minBy { fScore[it]!! }
            if (current.x == end.x && current.y == end.y) {
                val path = reconstructPath(cameFrom, current)
                var result = 0
                var dir = Dir4.E
                for (p in path.reversed().drop(1)) {
                    result++
                    if (dir != p.dir) {
                        result += 1000
                        dir = p.dir
                    }
                }
                return result
            }

            openSet -= current

            for (n in generateNeighbours(current, input)) {
                val g = gScore[current]!!+ 1 + if (current.dir != n.dir) 1000 else 0
                if (g < gScore.getOrDefault(n, Int.MAX_VALUE)) {
                    cameFrom[n] = current
                    gScore[n] = g
                    fScore[n] = g + h(n, end)
                    openSet += n
                }
            }
        }

        error("No solution")
    }

    override fun solvePart2(input: Grid<Char>): Int {
        TODO("Not yet implemented")
    }

}

fun main() {
    Harness.run(Day16)
}
