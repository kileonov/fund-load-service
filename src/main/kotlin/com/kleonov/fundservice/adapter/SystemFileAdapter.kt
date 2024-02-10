package com.kleonov.fundservice.adapter

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.File

@Component
class SystemFileAdapter {

    fun readFileLines(fileName: String): List<String> {
        return ClassPathResource("input.txt").file.readLines()
    }

    fun writeLinesToFile(lines: List<String>, fileName: String) {
        val outputFile = File(fileName)
        if (outputFile.exists()) {
            outputFile.delete()
        }
        outputFile.createNewFile()
        lines.forEach { outputFile.appendText("$it\n") }
    }
}