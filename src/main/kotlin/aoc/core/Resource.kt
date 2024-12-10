package aoc.core

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
    fun loadText(path: String): String = find(path).readText()

    /** Convert text to lines */
    fun splitIntoLines(text: String, allowBlankLines: Boolean = false): Sequence<String> {
        return text.lineSequence().filter { allowBlankLines || it.isNotBlank() }
    }

}
