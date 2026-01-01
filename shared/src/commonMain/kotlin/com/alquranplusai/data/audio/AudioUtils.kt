package com.alquranplusai.data.audio

/** Audio utility functions */
class AudioUtils {

    fun formatDuration(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val h = hours.toString().padStart(2, '0')
        val m = (minutes % 60).toString().padStart(2, '0')
        val s = (seconds % 60).toString().padStart(2, '0')
        return "$h:$m:$s"
    }

    fun getAudioFormat(url: String): String {
        return url.substringAfterLast(".")
    }
}
