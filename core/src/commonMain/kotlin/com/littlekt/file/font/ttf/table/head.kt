package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser
import kotlin.math.roundToInt

// global information about the font https://www.microsoft.com/typography/OTSPEC/head.htm
internal class HeadParser(val buffer: ByteBuffer, val start: Int) {

    fun parse(): Head {
        val parser: Parser = Parser(buffer, start)
        return Head(
            version = parser.parseVersion(),
            fontRevision = (parser.getParseFixed() * 1000).roundToInt() / 1000,
            checkSumAdjustment = parser.getParseUint32().toInt(),
            magicNumber = parser.getParseUint32().toInt().also { check(it == 0x5F0F3CF5) { "Font header has wrong magic number." } },
            flags = parser.getParseUint16(),
            unitsPerEm = parser.getParseUint16(),
            created = parser.getParseLongDateTime(),
            modified = parser.getParseLongDateTime(),
            xMin = parser.getParseInt16().toInt(),
            yMin = parser.getParseInt16().toInt(),
            xMax = parser.getParseInt16().toInt(),
            yMax = parser.getParseInt16().toInt(),
            macStyle = parser.getParseUint16(),
            lowestRecPPEM = parser.getParseUint16(),
            fontDirectionHint = parser.getParseInt16().toInt(),
            indexToLocFormat = parser.getParseInt16().toInt(),
            glyphDateFormat = parser.getParseInt16().toInt()
        )
    }
}

class Head(
    val version: Float,
    val fontRevision: Int,
    val checkSumAdjustment: Int,
    val magicNumber: Int,
    val flags: Int,
    val unitsPerEm: Int,
    val created: Int,
    val modified: Int,
    val xMin: Int,
    val yMin: Int,
    val xMax: Int,
    val yMax: Int,
    val macStyle: Int,
    val lowestRecPPEM: Int,
    val fontDirectionHint: Int,
    val indexToLocFormat: Int,
    val glyphDateFormat: Int
)
