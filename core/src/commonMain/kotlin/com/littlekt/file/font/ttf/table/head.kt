package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser
import kotlin.math.roundToInt

/**
 * The `head` table contains global information about the font.
 * https://www.microsoft.com/typography/OTSPEC/head.htm
 *
 * @author Colton Daily
 * @date 11/30/2021
 */
internal class HeadParser(val buffer: ByteBuffer, val start: Int) {

    fun parse(): Head {
        val p = Parser(buffer, start)
        return Head(
            version = p.parseVersion(),
            fontRevision = (p.getParseFixed() * 1000).roundToInt() / 1000,
            checkSumAdjustment = p.getParseUint32().toInt(),
            magicNumber =
                p.getParseUint32().toInt().also {
                    check(it == 0x5F0F3CF5) { "Font header has wrong magic number." }
                },
            flags = p.getParseUint16(),
            unitsPerEm = p.getParseUint16(),
            created = p.getParseLongDateTime(),
            modified = p.getParseLongDateTime(),
            xMin = p.getParseInt16().toInt(),
            yMin = p.getParseInt16().toInt(),
            xMax = p.getParseInt16().toInt(),
            yMax = p.getParseInt16().toInt(),
            macStyle = p.getParseUint16(),
            lowestRecPPEM = p.getParseUint16(),
            fontDirectionHint = p.getParseInt16().toInt(),
            indexToLocFormat = p.getParseInt16().toInt(),
            glyphDateFormat = p.getParseInt16().toInt()
        )
    }
}

/**
 * The `head` table contains global information about the font.
 * https://www.microsoft.com/typography/OTSPEC/head.htm
 *
 * @author Colton Daily
 * @date 11/30/2021
 */
internal class Head(
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
