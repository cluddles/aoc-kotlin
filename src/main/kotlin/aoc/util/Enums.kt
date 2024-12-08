package aoc.util

/** Returns the next enum value */
inline fun <reified T: Enum<T>> T.next(): T = move(1)

/** Returns the previous enum value */
inline fun <reified T: Enum<T>> T.prev(): T = move(-1)

/** Returns enum value located [offset] steps from this */
inline fun <reified T: Enum<T>> T.move(offset: Int): T {
    val values = enumValues<T>()
    val nextOrdinal = (ordinal + offset) % values.size
    return values[nextOrdinal]
}
