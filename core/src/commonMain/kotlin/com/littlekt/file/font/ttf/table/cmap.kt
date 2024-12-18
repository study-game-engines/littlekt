package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser
import com.littlekt.file.font.ttf.Type

// mappings from characters to glyphs https://www.microsoft.com/typography/OTSPEC/cmap.htm
internal class CmapParser(private val buffer: ByteBuffer, private val start: Int) {

    fun parse(): Cmap {
        val cmap: MutableCmap = MutableCmap()
        cmap.version = buffer.getUShort(start).toInt()
        check(cmap.version == 0) { "cmap table version should be 0!" }
        cmap.numTables = buffer.getUShort(start + 2).toInt()
        var offset: Int = -1
        for (index in cmap.numTables - 1 downTo 0) {
            val platformId: Int = buffer.getUShort(start + 4 + (index * 8)).toInt()
            val encodingId: Int = buffer.getUShort(start + 4 + (index * 8) + 2).toInt()
            if ((platformId == 3 && (encodingId == 0 || encodingId == 1 || encodingId == 10)) ||
                (platformId == 0 && (encodingId == 0 || encodingId == 1 || encodingId == 2 || encodingId == 3 || encodingId == 4))
            ) {
                offset = buffer.getUInt(start + 4 + (index * 8) + 4).toInt()
                break
            }
        }
        if (offset == -1) {
            throw IllegalStateException("No valid cmap sub-tables found")
        }
        val parser: Parser = Parser(buffer, start + offset)
        cmap.format = parser.getParseUint16()
        when (cmap.format) {
            12 -> parseFormat12(cmap, parser)
            4 -> parseFormat4(cmap, parser, start, offset)
            else -> throw IllegalStateException("Only format 4 and 12 cmap tables are supported (found format ${cmap.format}).")
        }
        return cmap.toCmap()
    }

    private fun parseFormat4(cmap: MutableCmap, parser: Parser, start: Int, offset: Int) {
        cmap.length = parser.getParseUint16()
        cmap.language = parser.getParseUint16()
        val segCount: Int = parser.getParseUint16() shr 1
        cmap.segCount = segCount
        parser.skip(Type.SHORT, 3)

        val endCountParser: Parser = Parser(buffer, start + offset + 14)
        val startCountParser: Parser = Parser(buffer, start + offset + 16 + segCount * 2)
        val idDeltaParser: Parser = Parser(buffer, start + offset + 16 + segCount * 4)
        val idRangeOffsetParser: Parser = Parser(buffer, start + offset + 16 + segCount * 6)
        var glyphIndexOffset: Int
        repeat(segCount - 1) {
            var glyphIndex: Int
            val endCount: Int = endCountParser.getParseUint16()
            val startCount: Int = startCountParser.getParseUint16()
            val idDelta: Int = idDeltaParser.getParseInt16().toInt()
            val idRangeOffset = idRangeOffsetParser.getParseUint16()
            for (c in startCount..endCount) {
                if (idRangeOffset != 0) {
                    glyphIndexOffset = (idRangeOffsetParser.offset + idRangeOffsetParser.relativeOffset - 2)
                    glyphIndexOffset += idRangeOffset
                    glyphIndexOffset += (c - startCount) * 2
                    glyphIndex = buffer.getUShort(glyphIndexOffset).toInt()
                    if (glyphIndex != 0) {
                        glyphIndex = (glyphIndex + idDelta) and 0xFFFF
                    }
                } else {
                    glyphIndex = (c + idDelta) and 0xFFFF
                }
                cmap.glyphIndexMap[c] = glyphIndex
            }
        }
    }

    private fun parseFormat12(cmap: MutableCmap, parser: Parser) {
        parser.getParseUint16()
        cmap.length = parser.getParseUint32().toInt()
        cmap.language = parser.getParseUint32().toInt()
        val groupCount = parser.getParseUint32()
        cmap.groupCount = groupCount.toInt()
        repeat(groupCount.toInt()) {
            val startCharCode: Int = parser.getParseUint32().toInt()
            val endCharCode: Int = parser.getParseUint32().toInt()
            var startGlyphId: Int = parser.getParseUint32().toInt()
            for (index in startCharCode..endCharCode) {
                cmap.glyphIndexMap[index] = startGlyphId
                startGlyphId++
            }
        }
    }
}

private class MutableCmap {
    var version: Int = 0
    var numTables: Int = 0
    var format: Int = 0
    var length: Int = 0
    var language: Int = 0
    var groupCount: Int = 0
    val glyphIndexMap: MutableMap<Int, Int> = mutableMapOf()
    var segCount: Int = 0
    fun toCmap(): Cmap = Cmap(version, numTables, format, length, language, groupCount, glyphIndexMap, segCount)
}

data class Cmap(val version: Int, val numTables: Int, val format: Int, val length: Int, val language: Int, val groupCount: Int, val glyphIndexMap: Map<Int, Int>, val segCount: Int) {
    override fun toString(): String = "Cmap(version=$version, numTables=$numTables, format=$format, length=$length, language=$language, groupCount=$groupCount, segCount=$segCount)"
}
