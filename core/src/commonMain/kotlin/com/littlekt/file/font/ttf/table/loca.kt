package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser
import kotlin.reflect.KFunction0

// offsets to the locations of the glyphs in the font https://www.microsoft.com/typography/OTSPEC/loca.htm
internal class LocaParser(val byteBuffer: ByteBuffer, val start: Int, val numGlyphs: Int, val shortVersion: Boolean) {

    fun parse(): IntArray {
        val parser: Parser = Parser(byteBuffer, start)
        val parseFunction: KFunction0<Int> = if (shortVersion) parser::getParseUint16 else parser::getParseInt32
        val glyphOffsets: IntArray = IntArray(numGlyphs + 1)
        for (i in 0 until numGlyphs) {
            var glyphOffset: Int = parseFunction()
            if (shortVersion) {
                glyphOffset *= 2
            }
            glyphOffsets[i] = glyphOffset
        }
        return glyphOffsets
    }

}
