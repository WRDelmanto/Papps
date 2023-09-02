package com.wrdelmanto.papps.utils.downloadExtension

interface Downloader {
    fun downloadFile(url: String): Long
}