package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser

// The `OS/2` table contains metrics required in OpenType fonts https://www.microsoft.com/typography/OTSPEC/os2.htm
internal class Os2Parser(val buffer: ByteBuffer, val start: Int) {

    fun parse(): Os2 {
        val parser: Parser = Parser(buffer, start)
        val version: Int = parser.getParseUint16()
        return Os2(
            version = version,
            xAvgCharWidth = parser.getParseInt16().toInt(),
            usWeightClass = parser.getParseUint16(),
            usWidthClass = parser.getParseUint16(),
            fsType = parser.getParseUint16(),
            ySubscriptXSize = parser.getParseInt16().toInt(),
            ySubscriptYSize = parser.getParseInt16().toInt(),
            ySubscriptXOffset = parser.getParseInt16().toInt(),
            ySubscriptYOffset = parser.getParseInt16().toInt(),
            ySuperscriptXSize = parser.getParseInt16().toInt(),
            ySuperscriptYSize = parser.getParseInt16().toInt(),
            ySuperscriptXOffset = parser.getParseInt16().toInt(),
            ySuperscriptYOffset = parser.getParseInt16().toInt(),
            yStrikeoutSize = parser.getParseInt16().toInt(),
            yStrikeoutPosition = parser.getParseInt16().toInt(),
            sFamilyClass = parser.getParseInt16().toInt(),
            panose = byteArrayOf(parser.parseByte(), parser.parseByte(), parser.parseByte(), parser.parseByte(), parser.parseByte(), parser.parseByte(), parser.parseByte(), parser.parseByte(), parser.parseByte(), parser.parseByte()),
            ulUnicodeRange1 = parser.getParseUint32().toInt(),
            ulUnicodeRange2 = parser.getParseUint32().toInt(),
            ulUnicodeRange3 = parser.getParseUint32().toInt(),
            ulUnicodeRange4 = parser.getParseUint32().toInt(),
            achVendID = charArrayOf(parser.parseChar(), parser.parseChar(), parser.parseChar(), parser.parseChar()).concatToString(),
            fsSelection = parser.getParseUint16(),
            usFirstCharIndex = parser.getParseUint16(),
            usLastCharIndex = parser.getParseUint16(),
            sTypoAscender = parser.getParseInt16().toInt(),
            sTypoDescender = parser.getParseInt16().toInt(),
            sTypoLineGap = parser.getParseInt16().toInt(),
            usWinAscent = parser.getParseUint16(),
            usWinDescent = parser.getParseUint16(),
            ulCodePageRange1 = if (version >= 1) parser.getParseUint32().toInt() else 0,
            ulCodePageRange2 = if (version >= 1) parser.getParseUint32().toInt() else 0,
            sxHeight = if (version >= 2) parser.getParseInt16().toInt() else 0,
            sCapHeight = if (version >= 2) parser.getParseInt16().toInt() else 0,
            usDefaultChar = if (version >= 2) parser.getParseUint16() else 0,
            usBreakChar = if (version >= 2) parser.getParseUint16() else 0,
            usMaxContent = if (version >= 2) parser.getParseUint16() else 0,
        )
    }
}

data class Os2(
    val version: Int,
    val xAvgCharWidth: Int,
    val usWeightClass: Int,
    val usWidthClass: Int,
    val fsType: Int,
    val ySubscriptXSize: Int,
    val ySubscriptYSize: Int,
    val ySubscriptXOffset: Int,
    val ySubscriptYOffset: Int,
    val ySuperscriptXSize: Int,
    val ySuperscriptYSize: Int,
    val ySuperscriptXOffset: Int,
    val ySuperscriptYOffset: Int,
    val yStrikeoutSize: Int,
    val yStrikeoutPosition: Int,
    val sFamilyClass: Int,
    val panose: ByteArray,
    val ulUnicodeRange1: Int,
    val ulUnicodeRange2: Int,
    val ulUnicodeRange3: Int,
    val ulUnicodeRange4: Int,
    val achVendID: String,
    val fsSelection: Int,
    val usFirstCharIndex: Int,
    val usLastCharIndex: Int,
    val sTypoAscender: Int,
    val sTypoDescender: Int,
    val sTypoLineGap: Int,
    val usWinAscent: Int,
    val usWinDescent: Int,
    val ulCodePageRange1: Int,
    val ulCodePageRange2: Int,
    val sxHeight: Int,
    val sCapHeight: Int,
    val usDefaultChar: Int,
    val usBreakChar: Int,
    val usMaxContent: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as Os2
        if (version != other.version) return false
        if (xAvgCharWidth != other.xAvgCharWidth) return false
        if (usWeightClass != other.usWeightClass) return false
        if (usWidthClass != other.usWidthClass) return false
        if (fsType != other.fsType) return false
        if (ySubscriptXSize != other.ySubscriptXSize) return false
        if (ySubscriptYSize != other.ySubscriptYSize) return false
        if (ySubscriptXOffset != other.ySubscriptXOffset) return false
        if (ySubscriptYOffset != other.ySubscriptYOffset) return false
        if (ySuperscriptXSize != other.ySuperscriptXSize) return false
        if (ySuperscriptYSize != other.ySuperscriptYSize) return false
        if (ySuperscriptXOffset != other.ySuperscriptXOffset) return false
        if (ySuperscriptYOffset != other.ySuperscriptYOffset) return false
        if (yStrikeoutSize != other.yStrikeoutSize) return false
        if (yStrikeoutPosition != other.yStrikeoutPosition) return false
        if (sFamilyClass != other.sFamilyClass) return false
        if (!panose.contentEquals(other.panose)) return false
        if (ulUnicodeRange1 != other.ulUnicodeRange1) return false
        if (ulUnicodeRange2 != other.ulUnicodeRange2) return false
        if (ulUnicodeRange3 != other.ulUnicodeRange3) return false
        if (ulUnicodeRange4 != other.ulUnicodeRange4) return false
        if (achVendID != other.achVendID) return false
        if (fsSelection != other.fsSelection) return false
        if (usFirstCharIndex != other.usFirstCharIndex) return false
        if (usLastCharIndex != other.usLastCharIndex) return false
        if (sTypoAscender != other.sTypoAscender) return false
        if (sTypoDescender != other.sTypoDescender) return false
        if (sTypoLineGap != other.sTypoLineGap) return false
        if (usWinAscent != other.usWinAscent) return false
        if (usWinDescent != other.usWinDescent) return false
        if (ulCodePageRange1 != other.ulCodePageRange1) return false
        if (ulCodePageRange2 != other.ulCodePageRange2) return false
        if (sxHeight != other.sxHeight) return false
        if (sCapHeight != other.sCapHeight) return false
        if (usDefaultChar != other.usDefaultChar) return false
        if (usBreakChar != other.usBreakChar) return false
        if (usMaxContent != other.usMaxContent) return false
        return true
    }

    override fun hashCode(): Int {
        var result: Int = version
        result = 31 * result + xAvgCharWidth
        result = 31 * result + usWeightClass
        result = 31 * result + usWidthClass
        result = 31 * result + fsType
        result = 31 * result + ySubscriptXSize
        result = 31 * result + ySubscriptYSize
        result = 31 * result + ySubscriptXOffset
        result = 31 * result + ySubscriptYOffset
        result = 31 * result + ySuperscriptXSize
        result = 31 * result + ySuperscriptYSize
        result = 31 * result + ySuperscriptXOffset
        result = 31 * result + ySuperscriptYOffset
        result = 31 * result + yStrikeoutSize
        result = 31 * result + yStrikeoutPosition
        result = 31 * result + sFamilyClass
        result = 31 * result + panose.contentHashCode()
        result = 31 * result + ulUnicodeRange1
        result = 31 * result + ulUnicodeRange2
        result = 31 * result + ulUnicodeRange3
        result = 31 * result + ulUnicodeRange4
        result = 31 * result + achVendID.hashCode()
        result = 31 * result + fsSelection
        result = 31 * result + usFirstCharIndex
        result = 31 * result + usLastCharIndex
        result = 31 * result + sTypoAscender
        result = 31 * result + sTypoDescender
        result = 31 * result + sTypoLineGap
        result = 31 * result + usWinAscent
        result = 31 * result + usWinDescent
        result = 31 * result + ulCodePageRange1
        result = 31 * result + ulCodePageRange2
        result = 31 * result + sxHeight
        result = 31 * result + sCapHeight
        result = 31 * result + usDefaultChar
        result = 31 * result + usBreakChar
        result = 31 * result + usMaxContent
        return result
    }

}
