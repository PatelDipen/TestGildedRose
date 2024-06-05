package com.gildedrose

import java.nio.file.Files
import java.nio.file.Path


object Utils {

    const val THIRTY_ITERATION_LOGS = "Thirty_Iteration_Logs"

    fun readFileContentAsString(filename:String): String {
        val directory = Path.of("", "src/test/resources")
        val file: Path = directory.resolve(filename)
        val string = Files.readString(file, Charsets.UTF_8)
        return string.replace("\r", "").trim()
    }
}