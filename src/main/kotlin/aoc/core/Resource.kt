package aoc.core

import java.io.BufferedReader
import java.io.File
import java.net.URL

object Resource {

    /**
     * Convert resource [path] to URL representation.
     *
     * Looks in both `src/main/resources` and `aoc-secret` directories
     */
    fun find(path: String): URL {
        return findResource(path) ?: findFile(path) ?: throw IllegalArgumentException("Resource not found: \"$path\"")
    }

    // Find under src/[main|test]/resources/...
    private fun findResource(path: String): URL? = javaClass.getResource("/$path")

    // Find under aoc-secret/...
    private fun findFile(path: String): URL? {
        val file = File("aoc-secret/$path")
        return if (file.exists()) file.toURI().toURL() else null
    }

    /** Load resource at [path] as one big String */
    fun asText(path: String): String = find(path).readText()

    /** Load resource at [path] as individual lines. Skips blank lines unless [allowBlankLines] is `true` */
    fun asLines(path: String, allowBlankLines: Boolean = false): List<String> = asBufferedReader(path).useLines {
        it.filter { l -> allowBlankLines || l.isNotBlank() }.toList()
    }

    /** Open resource at [path] as a BufferedReader */
    fun asBufferedReader(path: String): BufferedReader = find(path).openStream().bufferedReader()

}
