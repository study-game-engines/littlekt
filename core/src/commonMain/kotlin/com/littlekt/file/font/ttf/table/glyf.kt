package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.*
import com.littlekt.graphics.g2d.font.GlyphPath
import kotlin.math.floor

// glyphs in TrueType outline format http://www.microsoft.com/typography/otspec/glyf.htm
internal class GlyfParser(val buffer: ByteBuffer, val start: Int, val loca: IntArray, val fontReader: TtfFontReader) {

    fun parse(): GlyphSet {
        val glyphs: GlyphSet = GlyphSet()

        // The last element of the loca table is invalid.
        for (i in 0 until loca.size - 1) {
            val offset: Int = loca[i]
            val nextOffset: Int = loca[i + 1]
            if (offset != nextOffset) {
                glyphs[i] = TTfGlyphLoader(
                    fontReader = fontReader,
                    index = i,
                    unitsPerEm = fontReader.unitsPerEm,
                    parseGlyph = ::parseGlyph,
                    buffer = buffer,
                    position = start + offset,
                    buildPath = ::buildPath
                )
            } else {
                glyphs[i] = SimpleGlyphLoader(i, fontReader.unitsPerEm)
            }
        }
        return glyphs
    }

    private fun parseGlyph(glyph: MutableGlyph, buffer: ByteBuffer, start: Int) {
        val parser: Parser = Parser(buffer, start)
        glyph.numberOfContours = parser.getParseInt16().toInt()
        glyph.xMin = parser.getParseInt16().toInt()
        glyph.yMin = parser.getParseInt16().toInt()
        glyph.xMax = parser.getParseInt16().toInt()
        glyph.yMax = parser.getParseInt16().toInt()
        if (glyph.numberOfContours > 0) {
            val flags: MutableList<Int> = mutableListOf<Int>()
            var flag: Int
            glyph.endPointIndices.clear()
            glyph.instructions.clear()
            repeat(glyph.numberOfContours) {
                glyph.endPointIndices += parser.getParseUint16()
            }
            glyph.instructionLength = parser.getParseUint16()
            repeat(glyph.instructionLength) {
                glyph.instructions += parser.parseByte()
            }
            val numOfCoordinates: Int = glyph.endPointIndices[glyph.endPointIndices.size - 1] + 1
            var idx: Int = 0
            while (idx in 0 until numOfCoordinates) {
                flag = parser.parseUByte()
                flags += flag
                if ((flag and 8) > 0) {
                    val repeatCount: Int = parser.parseUByte()
                    repeat(repeatCount) {
                        flags += flag
                        idx += 1
                    }
                }
                idx++
            }
            check(flags.size == numOfCoordinates) { "Bad flags." }
            if (glyph.endPointIndices.isNotEmpty()) {
                if (numOfCoordinates > 0) {
                    for (index in 0 until numOfCoordinates) {
                        flag = flags[index]
                        glyph.points += MutablePoint(onCurve = (flag and 1) != 0, lastPointOfContour = glyph.endPointIndices.indexOf(index) >= 0)
                    }
                    var px: Int = 0
                    for (index in 0 until numOfCoordinates) {
                        flag = flags[index]
                        glyph.points[index].apply { x = parseGlyphCoord(parser, flag, px, 2, 16) }.also { px = it.x }
                    }
                    var py: Int = 0
                    for (index in 0 until numOfCoordinates) {
                        flag = flags[index]
                        glyph.points[index].apply { y = parseGlyphCoord(parser, flag, py, 4, 32) }.also { py = it.y }
                    }
                }
            }
        } else if (glyph.numberOfContours == 0) {
            glyph.points.clear()
        } else {
            glyph.isComposite = true
            glyph.points.clear()
            glyph.refs.clear()
            var moreRefs: Boolean = true
            var flags: Int = 0
            while (moreRefs) {
                flags = parser.getParseUint16()
                val ref: MutableGlyphReference = MutableGlyphReference(parser.getParseUint16(), 0, 0, 1f, 0f, 0f, 1f)
                if ((flags and 1) > 0) {
                    if ((flags and 2) > 0) {
                        ref.x = parser.getParseInt16().toInt()
                        ref.y = parser.getParseInt16().toInt()
                    } else {
                        ref.matchedPoints = intArrayOf(parser.getParseUint16(), parser.getParseUint16())
                    }
                } else {
                    if ((flags and 2) > 0) {
                        ref.x = parser.parseChar().code
                        ref.y = parser.parseChar().code
                    } else {
                        ref.matchedPoints = intArrayOf(parser.parseByte().toInt(), parser.parseByte().toInt())
                    }
                }
                when {
                    (flags and 8) > 0 -> {
                        ref.scaleX = parser.getParseF2Dot14().toFloat()
                        ref.scaleY = ref.scaleX
                    }

                    (flags and 64) > 0 -> {
                        ref.scaleX = parser.getParseF2Dot14().toFloat()
                        ref.scaleY = parser.getParseF2Dot14().toFloat()
                    }

                    (flags and 128) > 0 -> {
                        ref.scaleX = parser.getParseF2Dot14().toFloat()
                        ref.scale01 = parser.getParseF2Dot14().toFloat()
                        ref.scale10 = parser.getParseF2Dot14().toFloat()
                        ref.scaleY = parser.getParseF2Dot14().toFloat()
                    }
                }
                glyph.refs += ref
                moreRefs = (flags and 32) != 0
            }
            if (flags and 0x100 != 0) {
                glyph.instructionLength = parser.getParseUint16()
                glyph.instructions.clear()
                repeat(glyph.instructionLength) {
                    glyph.instructions += parser.parseByte()
                }
            }
        }
    }

    private fun parseGlyphCoord(p: Parser, flag: Int, prevValue: Int, shortVectorBitMask: Int, sameBitMask: Int): Int {
        var v: Int
        if ((flag and shortVectorBitMask) > 0) {
            val b = p.parseUByte()
            v = b
            if ((flag and sameBitMask) == 0) {
                v = -v
            }
            v += prevValue
        } else {
            v = if ((flag and sameBitMask) > 0) {
                prevValue
            } else {
                prevValue + p.getParseInt16()
            }
        }
        return v
    }

    fun buildPath(glyphSet: GlyphSet, glyph: MutableGlyph) {
        if (glyph.isComposite) {
            glyph.refs.forEach { ref ->
                val glyphRef: MutableGlyph = glyphSet[ref.glyphIndex].also { it.calcPath() }
                if (glyphRef.points.isNotEmpty()) {
                    val transformedPoints: MutableList<MutablePoint>
                    val matchedPoints: IntArray? = ref.matchedPoints
                    if (matchedPoints == null) {
                        // ref positioned by offset
                        transformedPoints = transformPoints(glyphRef.points, ref)
                    } else {
                        // ref positioned by matched points
                        check(matchedPoints[0] <= glyph.points.size - 1 && matchedPoints[1] <= glyphRef.points.size - 1) { "Matched points out of range in ${glyph.name}" }
                        val firstPt: MutablePoint = glyph.points[matchedPoints[0]]
                        var secondPt: MutablePoint = glyphRef.points[matchedPoints[1]]
                        val transformRef: MutableGlyphReference = MutableGlyphReference(glyphIndex = -1, x = 0, y = 0, scaleX = ref.scaleX, scale01 = ref.scale01, scale10 = ref.scale10, scaleY = ref.scaleY)
                        secondPt = transformPoints(listOf(secondPt), transformRef)[0]
                        transformRef.x = firstPt.x - secondPt.x
                        transformRef.y = firstPt.y - secondPt.y
                        transformedPoints = transformPoints(glyphRef.points, transformRef)
                    }
                    glyph.points.addAll(transformedPoints)
                }
                if (glyph.numberOfContours < 0) {
                    glyph.numberOfContours = 0
                }
                glyph.numberOfContours += glyphRef.numberOfContours
            }
        }
        calcPath(glyph)
    }

    fun transformPoints(points: List<MutablePoint>, ref: MutableGlyphReference): MutableList<MutablePoint> {
        val newPoints: MutableList<MutablePoint> = mutableListOf()
        for (point in points) {
            newPoints += MutablePoint(x = (ref.scaleX * point.x + ref.scale01 * point.y + ref.x).toInt(), y = (ref.scale10 * point.x + ref.scaleY * point.y + ref.y).toInt(), onCurve = point.onCurve, lastPointOfContour = point.lastPointOfContour)
        }
        return newPoints
    }

    fun calcPath(glyph: MutableGlyph) {
        if (glyph.points.isEmpty()) return
        val glyphPath: GlyphPath = GlyphPath(glyph.unitsPerEm)
        val contours: List<List<MutablePoint>> = getContours(glyph.points)
        for (contour in contours) {
            var curr: MutablePoint = contour[contour.size - 1]
            var next: MutablePoint = contour[0]
            var moved: Boolean = false
            if (curr.onCurve) {
                glyphPath.moveTo(curr.x.toFloat(), curr.y.toFloat())
            } else {
                if (next.onCurve) {
                    glyphPath.moveTo(next.x.toFloat(), next.y.toFloat())
                    moved = true
                } else {
                    // If both first and last points are off-curve, start at their middle.
                    val startX: Float = floor((curr.x + next.x) * 0.5f)
                    val startY: Float = floor((curr.y + next.y) * 0.5f)
                    glyphPath.moveTo(startX, startY)
                }
            }

            for (i in contour.indices) {
                curr = next
                next = contour[(i + 1) % contour.size]
                if (moved) {
                    moved = false
                    continue
                }
                if (curr.onCurve) {
                    // This is a straight line.
                    glyphPath.lineTo(curr.x.toFloat(), curr.y.toFloat())
                } else {
                    var next2: Pair<Float, Float> = next.x.toFloat() to next.y.toFloat()
                    if (next.onCurve) {
                        moved = true
                    } else {
                        next2 = floor((curr.x + next.x) * 0.5f) to floor((curr.y + next.y) * 0.5f)
                    }
                    glyphPath.quadTo(curr.x.toFloat(), curr.y.toFloat(), next2.first, next2.second)
                }
            }
            glyphPath.close()
        }
        glyph.path = glyphPath
    }

    fun getContours(points: List<MutablePoint>): List<List<MutablePoint>> {
        val contours: MutableList<List<MutablePoint>> = mutableListOf<List<MutablePoint>>()
        var current: MutableList<MutablePoint> = mutableListOf<MutablePoint>()
        for (point in points) {
            current += point
            if (point.lastPointOfContour) {
                contours += current
                current = mutableListOf()
            }
        }
        check(current.isEmpty()) { "There are still points left in the current contour." }
        return contours
    }

}
