package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser

/**
 * @author Colton Daily
 * @date 11/30/2021
 */
internal class HheaParser(val buffer: ByteBuffer, val start: Int) {

    fun parse(): Hhea {
        val p = Parser(buffer, start)
        return Hhea(
            version = p.parseVersion(),
            ascender = p.getParseInt16().toInt(),
            descender = p.getParseInt16().toInt(),
            lineGap = p.getParseInt16().toInt(),
            advanceWidthMax = p.getParseUint16(),
            minLeftSideBearing = p.getParseInt16().toInt(),
            minRightSideBearing = p.getParseInt16().toInt(),
            xMaxExtent = p.getParseInt16().toInt(),
            caretSlopeRise = p.getParseInt16().toInt(),
            caretSlopeRun = p.getParseInt16().toInt(),
            caretOffset = p.getParseInt16().toInt().also { p.relativeOffset += 8 },
            metricDataFormat = p.getParseInt16().toInt(),
            numberOfHMetrics = p.getParseUint16(),
        )
    }
}

internal data class Hhea(
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
