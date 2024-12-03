package com.littlekt.file.font.ttf

import com.littlekt.graphics.g2d.font.ContourPoint
import com.littlekt.graphics.g2d.font.GlyphPath
import com.littlekt.graphics.g2d.font.GlyphReference
import com.littlekt.graphics.g2d.font.TtfGlyph
import com.littlekt.math.Rect

class MutableGlyph(var index: Int, var unitsPerEm: Int) {

    var name: String? = null
    var xMin: Int = 0
    var yMin: Int = 0
    var xMax: Int = 0
    var yMax: Int = 0
    var advanceWidth: Float = 0f
    var leftSideBearing: Int = 0
    var numberOfContours: Int = 0
    val endPointIndices = mutableListOf<Int>()
    var instructionLength: Int = 0
    val instructions = mutableListOf<Byte>()
    val points = mutableListOf<MutablePoint>()
    val refs = mutableListOf<MutableGlyphReference>()
    var isComposite: Boolean = false
    var codePoint: Int = -1
    var bounds: Rect? = null
    var calcPath: () -> Unit = {}
    var path: GlyphPath = GlyphPath(unitsPerEm)
    val unicodes = mutableListOf<Int>()
    var unicode: Int = 0

    fun addUnicode(unicode: Int) {
        if (unicodes.isEmpty()) {
            this.unicode = unicode
        }
        unicodes.add(unicode)
    }

    fun toImmutable(): TtfGlyph {
        val points: List<ContourPoint> = points.map { it.toImmutable() }
        val refs: List<GlyphReference> = refs.map { it.toImmutable() }
        return TtfGlyph(name, index, xMin, yMin, xMax, yMax, advanceWidth, leftSideBearing, numberOfContours, unicode, unicodes.toList(), path, endPointIndices.toList(), instructionLength, instructions.toList(), points, refs, isComposite, unitsPerEm)
    }

}

data class MutablePoint(var x: Int = 0, var y: Int = 0, var onCurve: Boolean = false, var lastPointOfContour: Boolean = false) {
    fun toImmutable(): ContourPoint = ContourPoint(x, y, onCurve, lastPointOfContour)
}

data class MutableGlyphReference(val glyphIndex: Int, var x: Int, var y: Int, var scaleX: Float, var scale01: Float, var scale10: Float, var scaleY: Float, var matchedPoints: IntArray? = null) {

    fun toImmutable(): GlyphReference {
        return GlyphReference(glyphIndex, x, y, scaleX, scale01, scale10, scaleY, matchedPoints)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as MutableGlyphReference
        if (glyphIndex != other.glyphIndex) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (scaleX != other.scaleX) return false
        if (scale01 != other.scale01) return false
        if (scale10 != other.scale10) return false
        if (scaleY != other.scaleY) return false
        if (!matchedPoints.contentEquals(other.matchedPoints)) return false
        return true
    }

    override fun hashCode(): Int {
        var result: Int = glyphIndex
        result = 31 * result + x
        result = 31 * result + y
        result = 31 * result + scaleX.hashCode()
        result = 31 * result + scale01.hashCode()
        result = 31 * result + scale10.hashCode()
        result = 31 * result + scaleY.hashCode()
        result = 31 * result + matchedPoints.contentHashCode()
        return result
    }

}
