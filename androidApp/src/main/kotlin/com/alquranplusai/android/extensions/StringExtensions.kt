package com.alquranplusai.android.extensions

fun String.toArabicNumerals(): String {
    val arabicNumerals = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
    var result = this
    for (i in 0..9) {
        result = result.replace(i.toString(), arabicNumerals[i])
    }
    return result
}

fun String.fromArabicNumerals(): String {
    val arabicNumerals = arrayOf("٠", "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩")
    var result = this
    for (i in 0..9) {
        result = result.replace(arabicNumerals[i], i.toString())
    }
    return result
}

fun String.capitalizeWords(): String {
    return split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
