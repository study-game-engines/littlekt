package com.littlekt.file.font.ttf

import com.littlekt.file.ByteBuffer

typealias GlyphLoader = () -> MutableGlyph

fun SimpleGlyphLoader(index: Int, unitsPerEm: Int): GlyphLoader {
    return { MutableGlyph(index, unitsPerEm) }
}

fun TTfGlyphLoader(fontReader: TtfFontReader, index: Int, unitsPerEm: Int, parseGlyph: (MutableGlyph, ByteBuffer, Int) -> Unit, buffer: ByteBuffer, position: Int, buildPath: (GlyphSet, MutableGlyph) -> Unit): GlyphLoader {
    return {
        val glyph: MutableGlyph = MutableGlyph(index, unitsPerEm)
        glyph.calcPath = {
            parseGlyph.invoke(glyph, buffer, position)
            buildPath(fontReader.glyphs, glyph)
        }
        glyph
    }
}

class GlyphSet : Iterable<MutableGlyph> {

    private val glyphLoader: MutableMap<Int, () -> MutableGlyph> = mutableMapOf()
    private val glyphs: MutableMap<Int, MutableGlyph> = mutableMapOf()
    var size: Int = 0
        private set

    override fun iterator(): Iterator<MutableGlyph> {
        return glyphs.values.iterator()
    }

    operator fun get(index: Int): MutableGlyph {
        val glyph: MutableGlyph = glyphs.getOrPut(index) {
            glyphLoader[index]?.invoke() ?: error("Unable to retrieve or load glyph of index $index")
        }
        return glyph
    }

    operator fun set(index: Int, loader: GlyphLoader) {
        glyphLoader[index] = loader
        size++
    }

    override fun toString(): String {
        return "GlyphSet(glyphs=$glyphs, size=$size)"
    }

}
