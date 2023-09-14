package com.wrdelmanto.papps.utils.downloadExtension

fun interface Downloader {
    fun downloadFile(url: String): Long
}