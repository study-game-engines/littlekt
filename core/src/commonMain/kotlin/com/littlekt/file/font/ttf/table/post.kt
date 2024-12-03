package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Encoding
import com.littlekt.file.font.ttf.Parser

// additional PostScript information, such as glyph names https://www.microsoft.com/typography/OTSPEC/post.htm
class PostParser(val buffer: ByteBuffer, val start: Int) {

    fun parse(): Post {
        val parser: Parser = Parser(buffer, start)
        val post = MutablePost().apply {
            version = parser.parseVersion()
            italicAngle = parser.getParseFixed()
            underlinePosition = parser.getParseInt16().toInt()
            underlineThickness = parser.getParseInt16().toInt()
            isFixedPitch = parser.getParseUint32().toInt()
            minMemType42 = parser.getParseUint32().toInt()
            maxMemType42 = parser.getParseUint32().toInt()
            minMemType1 = parser.getParseUint32().toInt()
            maxMemType1 = parser.getParseUint32().toInt()
            when (version) {
                1f -> {
                    names = Encoding.STANDARD_NAMES.copyOf()
                }

                2f -> {
                    numberOfGlyphs = parser.getParseUint16()
                    glyphNameIndex = IntArray(numberOfGlyphs) { parser.getParseUint16() }
                    val nameList: MutableList<String> = mutableListOf()
                    for (index in 0 until numberOfGlyphs) {
                        if (glyphNameIndex[index] >= Encoding.STANDARD_NAMES.size) {
                            val nameLength = parser.parseUByte()
                            nameList += parser.parseString(nameLength)
                        }
                    }
                    names = nameList.toTypedArray()
                }

                2.5f -> {
                    numberOfGlyphs = parser.getParseUint16()
                    offset = CharArray(numberOfGlyphs) { parser.parseChar() }
                }
            }
        }
        return post.toPost()
    }
}

class MutablePost {
    var version: Float = 0f
    var italicAngle: Float = 0f
    var underlinePosition: Int = 0
    var underlineThickness: Int = 0
    var isFixedPitch: Int = 0
    var minMemType42: Int = 0
    var maxMemType42: Int = 0
    var minMemType1: Int = 0
    var maxMemType1: Int = 0
    var names: Array<String> = arrayOf()
    var numberOfGlyphs: Int = 0
    var glyphNameIndex: IntArray = intArrayOf()
    var offset: CharArray = charArrayOf()
    fun toPost(): Post = Post(version, italicAngle, underlinePosition, underlineThickness, isFixedPitch, minMemType42, maxMemType42, minMemType1, maxMemType1, names, numberOfGlyphs, glyphNameIndex, offset)
}

data class Post(
    val version: Float,
    val italicAngle: Float,
    val underlinePosition: Int,
    val underlineThickness: Int,
    val isFixedPitch: Int,
    val minMemType42: Int,
    val maxMemType42: Int,
    val minMemType1: Int,
    val maxMemType1: Int,
    val names: Array<String>,
    val numberOfGlyphs: Int,
    val glyphNameIndex: IntArray,
    val offset: CharArray
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Post
        if (version != other.version) return false
        if (italicAngle != other.italicAngle) return false
        if (underlinePosition != other.underlinePosition) return false
        if (underlineThickness != other.underlineThickness) return false
        if (isFixedPitch != other.isFixedPitch) return false
        if (minMemType42 != other.minMemType42) return false
        if (maxMemType42 != other.maxMemType42) return false
        if (minMemType1 != other.minMemType1) return false
        if (maxMemType1 != other.maxMemType1) return false
        if (!names.contentEquals(other.names)) return false
        if (numberOfGlyphs != other.numberOfGlyphs) return false
        if (!glyphNameIndex.contentEquals(other.glyphNameIndex)) return false
        if (!offset.contentEquals(other.offset)) return false
        return true
    }

    override fun hashCode(): Int {
        var result: Int = version.toInt()
        result = 31 * result + italicAngle.hashCode()
        result = 31 * result + underlinePosition
        result = 31 * result + underlineThickness
        result = 31 * result + isFixedPitch
        result = 31 * result + minMemType42
        result = 31 * result + maxMemType42
        result = 31 * result + minMemType1
        result = 31 * result + maxMemType1
        result = 31 * result + names.contentHashCode()
        result = 31 * result + numberOfGlyphs
        result = 31 * result + glyphNameIndex.contentHashCode()
        result = 31 * result + offset.contentHashCode()
        return result
    }

    override fun toString(): String {
        return "Post(version=$version, italicAngle=$italicAngle, underlinePosition=$underlinePosition, underlineThickness=$underlineThickness, isFixedPitch=$isFixedPitch, minMemType42=$minMemType42, maxMemType42=$maxMemType42, minMemType1=$minMemType1, maxMemType1=$maxMemType1, numberOfGlyphs=$numberOfGlyphs)"
    }

}
