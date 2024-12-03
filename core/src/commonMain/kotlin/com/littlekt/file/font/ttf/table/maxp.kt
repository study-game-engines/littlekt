package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser

// memory requirements for the font. We need it just to get the number of glyphs in the font. https://www.microsoft.com/typography/OTSPEC/maxp.htm
class MaxpParser(val buffer: ByteBuffer, val start: Int) {
    fun parse(): Maxp {
        val parser: Parser = Parser(buffer, start)
        val version: Float = parser.parseVersion()
        return Maxp(
            version = version,
            numGlyphs = parser.getParseUint16(),
            maxPoints = if (version == 1f) parser.getParseUint16() else 0,
            maxContours = if (version == 1f) parser.getParseUint16() else 0,
            maxCompositePoints = if (version == 1f) parser.getParseUint16() else 0,
            maxCompositeContours = if (version == 1f) parser.getParseUint16() else 0,
            maxZones = if (version == 1f) parser.getParseUint16() else 0,
            maxTwilightPoints = if (version == 1f) parser.getParseUint16() else 0,
            maxStorage = if (version == 1f) parser.getParseUint16() else 0,
            maxFunctionDefs = if (version == 1f) parser.getParseUint16() else 0,
            maxInstructionDefs = if (version == 1f) parser.getParseUint16() else 0,
            maxStackElements = if (version == 1f) parser.getParseUint16() else 0,
            maxSizeOfInstructions = if (version == 1f) parser.getParseUint16() else 0,
            maxComponentElements = if (version == 1f) parser.getParseUint16() else 0,
            maxComponentDepth = if (version == 1f) parser.getParseUint16() else 0,
        )
    }
}

data class Maxp(
    val version: Float,
    val numGlyphs: Int,
    val maxPoints: Int,
    val maxContours: Int,
    val maxCompositePoints: Int,
    val maxCompositeContours: Int,
    val maxZones: Int,
    val maxTwilightPoints: Int,
    val maxStorage: Int,
    val maxFunctionDefs: Int,
    val maxInstructionDefs: Int,
    val maxStackElements: Int,
    val maxSizeOfInstructions: Int,
    val maxComponentElements: Int,
    val maxComponentDepth: Int
)
