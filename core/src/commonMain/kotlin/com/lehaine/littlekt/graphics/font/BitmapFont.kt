package com.lehaine.littlekt.graphics.font

import com.lehaine.littlekt.graph.node.component.HAlign
import com.lehaine.littlekt.graphics.Color
import com.lehaine.littlekt.graphics.SpriteBatch
import com.lehaine.littlekt.graphics.Texture
import com.lehaine.littlekt.graphics.TextureSlice
import com.lehaine.littlekt.math.Rect
import com.lehaine.littlekt.math.geom.Angle
import kotlin.math.max

/**
 * @author Colt Daily
 * @date 1/5/22
 */
class BitmapFont(
    val fontSize: Float,
    val lineHeight: Float,
    val base: Float,
    val textures: List<Texture>,
    val glyphs: Map<Int, Glyph>,
    val kernings: Map<Int, Kerning>,
    val pages: Int = 1
) : Font {

    private val slices = glyphs
    private val cache = BitmapFontCache(this)

    /**
     * The name of the font or null.
     */
    var name: String? = null

    /**
     * The width of space character.
     */
    var spaceWidth: Float = 0f

    override val metrics: FontMetrics = run {
        val ascent = base
        val baseline = 0f
        val descent = lineHeight - base
        FontMetrics(
            fontSize, ascent, ascent, baseline, -descent, -descent, 0f,
            maxWidth = run {
                var width = 0f
                for (glyph in glyphs.values) width = max(width, glyph.slice.width.toFloat())
                width
            }
        )
    }

    override val glyphMetrics: Map<Int, GlyphMetrics> = glyphs.entries.map {
        val glyph = it.value
        GlyphMetrics(
            size = fontSize,
            code = glyph.id,
            bounds = Rect(
                glyph.xoffset.toFloat(),
                glyph.yoffset.toFloat(),
                glyph.slice.width.toFloat(),
                glyph.slice.height.toFloat()
            ),
            xAdvance = glyph.xadvance.toFloat(),
            u = glyph.slice.u,
            v = glyph.slice.v,
            u2 = glyph.slice.u2,
            v2 = glyph.slice.v2,
            page = glyph.page
        )
    }.associateBy { it.code }

    override var wrapChars: CharSequence = ""

    override fun getKerning(first: Int, second: Int): Kerning? {
        return kernings[Kerning.buildKey(first, second)]
    }

    fun draw(
        batch: SpriteBatch,
        text: CharSequence,
        x: Float,
        y: Float,
        rotation: Angle = Angle.ZERO,
        color: Color = Color.WHITE,
        targetWidth: Float = 0f,
        align: HAlign = HAlign.LEFT,
        wrap: Boolean = false
    ) {
        cache.setText(text, x, y, 1f, rotation, color, targetWidth, align, wrap)
        cache.draw(batch) // TODO impl multiple page font
    }

    data class Glyph(
        val fontSize: Float,
        val id: Int,
        val slice: TextureSlice,
        val xoffset: Int,
        val yoffset: Int,
        val xadvance: Int,
        val page: Int
    )

}