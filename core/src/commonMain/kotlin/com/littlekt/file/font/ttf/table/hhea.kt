package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser

class HheaParser(val buffer: ByteBuffer, val start: Int) {

    fun parse(): Hhea {
        val parser: Parser = Parser(buffer, start)
        return Hhea(
            version = parser.parseVersion(),
            ascender = parser.getParseInt16().toInt(),
            descender = parser.getParseInt16().toInt(),
            lineGap = parser.getParseInt16().toInt(),
            advanceWidthMax = parser.getParseUint16(),
            minLeftSideBearing = parser.getParseInt16().toInt(),
            minRightSideBearing = parser.getParseInt16().toInt(),
            xMaxExtent = parser.getParseInt16().toInt(),
            caretSlopeRise = parser.getParseInt16().toInt(),
            caretSlopeRun = parser.getParseInt16().toInt(),
            caretOffset = parser.getParseInt16().toInt().also { parser.relativeOffset += 8 },
            metricDataFormat = parser.getParseInt16().toInt(),
            numberOfHMetrics = parser.getParseUint16(),
        )
    }

}

data class Hhea(
    val version: Float,
    val ascender: Int,
    val descender: Int,
    val lineGap: Int,
    val advanceWidthMax: Int,
    val minLeftSideBearing: Int,
    val minRightSideBearing: Int,
    val xMaxExtent: Int,
    val caretSlopeRise: Int,
    val caretSlopeRun: Int,
    val caretOffset: Int,
    val metricDataFormat: Int,
    val numberOfHMetrics: Int
)
