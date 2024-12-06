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
        val fromResource = javaClass.getResource("/$path")
        if (fromResource == null) {
            val file = File("aoc-secret/$path")
            if (file.exists()) return file.toURI().toURL()
        }
        throw IllegalArgumentException("Resource not found: \"$path\"")
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
