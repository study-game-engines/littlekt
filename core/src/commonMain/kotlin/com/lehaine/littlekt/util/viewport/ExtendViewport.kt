package com.lehaine.littlekt.util.viewport

import com.lehaine.littlekt.Context
import com.lehaine.littlekt.util.Scaler
import kotlin.math.roundToInt

/**
 * @author Colton Daily
 * @date 12/27/2021
 */
class ExtendViewport(virtualWidth: Int, virtualHeight: Int) : Viewport() {
    init {
        this.virtualWidth = virtualWidth
        this.virtualHeight = virtualHeight
    }

    override fun update(width: Int, height: Int, context: Context) {
        var worldWidth = virtualWidth.toFloat()
        var worldHeight = virtualHeight.toFloat()

        val scaled = Scaler.Fit().apply(virtualWidth, virtualHeight, width, height)
        var viewportWidth = scaled.x.roundToInt()
        var viewportHeight = scaled.y.roundToInt()
        if (viewportWidth < width) {
            val toViewportSpace = viewportHeight / worldHeight
            val toWorldSpace = worldHeight / viewportHeight
            val lengthen = (width - viewportWidth) * toWorldSpace
            worldWidth += lengthen
            viewportWidth += (lengthen * toViewportSpace).roundToInt()
        } else if (viewportHeight < height) {
            val toViewportSpace = viewportWidth / worldWidth
            val toWorldSpace = worldWidth / viewportWidth
            val lengthen = (height - viewportHeight) * toWorldSpace
            worldHeight += lengthen
            viewportHeight += (lengthen * toViewportSpace).roundToInt()
        }

        virtualWidth = worldWidth.toInt()
        virtualHeight = worldHeight.toInt()
        set((width - viewportWidth) / 2, (height - viewportHeight) / 2, viewportWidth, viewportHeight)
        apply(context)
    }
}