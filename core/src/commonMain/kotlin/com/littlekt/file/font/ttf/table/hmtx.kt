package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.GlyphSet
import com.littlekt.file.font.ttf.Parser

// horizontal metrics for all glyphs https://www.microsoft.com/typography/OTSPEC/hmtx.htm
internal class HmtxParser(val buffer: ByteBuffer, val start: Int, val numOfHMetrics: Int, val numGlyphs: Int, val glyphs: GlyphSet) {
    fun parse() {
        var advanceWidth: Int = 0
        var leftSideBearing: Int = 0
        val parser: Parser = Parser(buffer, start)
        for (index in 0 until numGlyphs) {
            if (index < numOfHMetrics) {
                advanceWidth = parser.getParseUint16()
                leftSideBearing = parser.getParseInt16().toInt()
            }
            glyphs[index].apply {
                this.advanceWidth = advanceWidth.toFloat()
                this.leftSideBearing = leftSideBearing
            }
        }
    }
}
