package com.lehaine.littlekt.graphics.font

import com.lehaine.littlekt.file.Uint8Buffer
import com.lehaine.littlekt.file.createMixedBuffer
import com.lehaine.littlekt.file.font.ttf.TtfFontReader

/**
 * @author Colton Daily
 * @date 12/2/2021
 */
class TtfFont(val chars: CharArray) {
    constructor(chars: String = CharacterSets.LATIN_ALL) : this(chars.map { it }.toCharArray())

    private val glyphCache = mutableMapOf<Int, Glyph>()
    val glyphs: Map<Int, Glyph> get() = glyphCache

    fun load(data: Uint8Buffer) {
        val buffer = createMixedBuffer(data.toArray())
        val reader = TtfFontReader().also {
            it.parse(buffer)
        }
        chars.forEach { char ->
            glyphCache[char.code] = reader[char]
        }
    }
}