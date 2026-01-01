package com.alquranplusai.utils

/**
 * Extension functions for collections
 */

fun <T> List<T>.second(): T {
    if (size < 2) throw NoSuchElementException("List has less than 2 elements.")
    return this[1]
}

fun <T> List<T>.secondOrNull(): T? {
    return if (size >= 2) this[1] else null
}

fun <T> List<T>.third(): T {
    if (size < 3) throw NoSuchElementException("List has less than 3 elements.")
    return this[2]
}

fun <T> List<T>.thirdOrNull(): T? {
    return if (size >= 3) this[2] else null
}

fun <T> List<T>.chunked(size: Int, step: Int): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var index = 0
    while (index < this.size) {
        val end = minOf(index + size, this.size)
        result.add(this.subList(index, end))
        index += step
    }
    return result
}

fun <T> List<T>.takeRandom(count: Int): List<T> {
    return this.shuffled().take(count)
}
