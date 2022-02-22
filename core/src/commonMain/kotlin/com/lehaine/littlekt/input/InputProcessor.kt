package com.lehaine.littlekt.input

import com.lehaine.littlekt.util.fastForEach

/**
 * @author Colton Daily
 * @date 11/17/2021
 */
interface InputProcessor {
    fun keyDown(key: Key): Boolean = false

    fun keyUp(key: Key): Boolean = false

    fun keyRepeat(key: Key): Boolean = false

    fun charTyped(character: Char): Boolean = false

    fun touchDown(screenX: Float, screenY: Float, pointer: Pointer): Boolean = false

    fun touchUp(screenX: Float, screenY: Float, pointer: Pointer): Boolean = false

    fun touchDragged(screenX: Float, screenY: Float, pointer: Pointer): Boolean = false

    fun mouseMoved(screenX: Float, screenY: Float): Boolean = false

    fun scrolled(amountX: Float, amountY: Float): Boolean = false

    fun gamepadButtonPressed(button: GameButton, pressure: Float, gamepad: Int): Boolean = false

    fun gamepadButtonReleased(button: GameButton, gamepad: Int): Boolean = false

    fun gamepadJoystickMoved(stick: GameStick, xAxis: Float, yAxis: Float, gamepad: Int): Boolean = false

    fun gamepadTriggerChanged(button: GameButton, pressure: Float, gamepad: Int): Boolean = false
}

class InputProcessBuilder {
    private val keyDown = mutableListOf<(Key) -> Boolean>()
    private val keyUp = mutableListOf<(Key) -> Boolean>()
    private val keyRepeat = mutableListOf<(Key) -> Boolean>()
    private val charTyped = mutableListOf<(Char) -> Boolean>()
    private val touchDown = mutableListOf<(Float, Float, Pointer) -> Boolean>()
    private val touchUp = mutableListOf<(Float, Float, Pointer) -> Boolean>()
    private val touchDragged = mutableListOf<(Float, Float, Pointer) -> Boolean>()
    private val mouseMoved = mutableListOf<(Float, Float) -> Boolean>()
    private val scrolled = mutableListOf<(Float, Float) -> Boolean>()
    private val gameButtonPressed = mutableListOf<(GameButton, Float, Int) -> Boolean>()
    private val gameButtonReleased = mutableListOf<(GameButton, Int) -> Boolean>()
    private val gameJoystickMoved = mutableListOf<(GameStick, Float, Float, Int) -> Boolean>()
    private val gamepadTriggerChanged = mutableListOf<(GameButton, Float, Int) -> Boolean>()

    fun onKeyDownHandle(action: (key: Key) -> Boolean) {
        keyDown += action
    }

    fun onKeyDown(action: (key: Key) -> Unit) {
        keyDown += {
            action(it)
            false
        }
    }

    fun onKeyUpHandle(action: (key: Key) -> Boolean) {
        keyUp += action
    }

    fun onKeyUp(action: (key: Key) -> Unit) {
        keyUp += {
            action(it)
            false
        }
    }

    fun onKeyRepeatHandle(action: (key: Key) -> Boolean) {
        keyRepeat += action
    }

    fun onKeyRepeat(action: (key: Key) -> Unit) {
        keyRepeat += {
            action(it)
            false
        }
    }

    fun onCharTypedHandle(action: (character: Char) -> Boolean) {
        charTyped += action
    }

    fun onCharTyped(action: (character: Char) -> Unit) {
        charTyped += {
            action(it)
            false
        }
    }

    fun onTouchDownHandle(action: (screenX: Float, screenY: Float, pointer: Pointer) -> Boolean) {
        touchDown += action
    }

    fun onTouchDown(action: (screenX: Float, screenY: Float, pointer: Pointer) -> Unit) {
        touchDown += { screenX: Float, screenY: Float, pointer: Pointer ->
            action(screenX, screenY, pointer)
            false
        }
    }

    fun onTouchUpHandle(action: (screenX: Float, screenY: Float, pointer: Pointer) -> Boolean) {
        touchUp += action
    }

    fun onTouchUp(action: (screenX: Float, screenY: Float, pointer: Pointer) -> Unit) {
        touchUp += { screenX, screenY, pointer ->
            action(screenX, screenY, pointer)
            false
        }
    }

    fun onTouchDraggedHandle(action: (screenX: Float, screenY: Float, pointer: Pointer) -> Boolean) {
        touchDragged += action
    }

    fun onTouchDragged(action: (screenX: Float, screenY: Float, pointer: Pointer) -> Unit) {
        touchDragged += { screenX, screenY, pointer ->
            action(screenX, screenY, pointer)
            false
        }
    }

    fun onMouseMovedHandle(action: (screenX: Float, screenY: Float) -> Boolean) {
        mouseMoved += action
    }

    fun onMouseMoved(action: (screenX: Float, screenY: Float) -> Unit) {
        mouseMoved += { screenX, screenY ->
            action(screenX, screenY)
            false
        }
    }

    fun onScrolledHandle(action: (amountX: Float, amountY: Float) -> Boolean) {
        scrolled += action
    }

    fun onScrolled(action: (amountX: Float, amountY: Float) -> Unit) {
        scrolled += { amountX, amountY ->
            action(amountX, amountY)
            false
        }
    }

    fun onGamepadButtonPressedHandle(action: (button: GameButton, pressure: Float, gamepad: Int) -> Boolean) {
        gameButtonPressed += action
    }

    fun onGamepadButtonPressed(action: (button: GameButton, pressure: Float, gamepad: Int) -> Unit) {
        gameButtonPressed += { button, pressure, gamepad ->
            action(button, pressure, gamepad)
            false
        }
    }

    fun onGamepadButtonReleasedHandle(action: (button: GameButton, gamepad: Int) -> Boolean) {
        gameButtonReleased += action
    }

    fun onGamepadButtonReleased(action: (button: GameButton, gamepad: Int) -> Unit) {
        gameButtonReleased += { button, gamepad ->
            action(button, gamepad)
            false
        }
    }

    fun onGamepadJoystickMovedHandle(action: (stick: GameStick, xAxis: Float, yAxis: Float, gamepad: Int) -> Boolean) {
        gameJoystickMoved += action
    }

    fun onGamepadJoystickMoved(action: (stick: GameStick, xAxis: Float, yAxis: Float, gamepad: Int) -> Unit) {
        gameJoystickMoved += { stick, xAxis, yAxis, gamepad ->
            action(stick, xAxis, yAxis, gamepad)
            false
        }
    }

    fun onGamepadTriggerChangedHandle(action: (button: GameButton, pressure: Float, gamepad: Int) -> Boolean) {
        gamepadTriggerChanged += action
    }

    fun onGamepadTriggerChanged(action: (button: GameButton, pressure: Float, gamepad: Int) -> Unit) {
        gamepadTriggerChanged += { button, pressure, gamepad ->
            action(button, pressure, gamepad)
            false
        }
    }

    fun build() = object : InputProcessor {

        override fun keyDown(key: Key): Boolean {
            var handled = false
            keyDown.fastForEach { if (it.invoke(key)) handled = true }
            return handled
        }

        override fun keyUp(key: Key): Boolean {
            var handled = false
            keyUp.fastForEach { if (it.invoke(key)) handled = true }
            return handled
        }

        override fun keyRepeat(key: Key): Boolean {
            var handled = false
            keyRepeat.fastForEach { if (it.invoke(key)) handled = true }
            return handled
        }

        override fun charTyped(character: Char): Boolean {
            var handled = false
            charTyped.fastForEach { if (it.invoke(character)) handled = true }
            return handled
        }

        override fun touchDown(screenX: Float, screenY: Float, pointer: Pointer): Boolean {
            var handled = false
            touchDown.fastForEach { if (it.invoke(screenX, screenY, pointer)) handled = true }
            return handled
        }

        override fun touchUp(screenX: Float, screenY: Float, pointer: Pointer): Boolean {
            var handled = false
            touchUp.fastForEach { if (it.invoke(screenX, screenY, pointer)) handled = true }
            return handled
        }

        override fun touchDragged(screenX: Float, screenY: Float, pointer: Pointer): Boolean {
            var handled = false
            touchDragged.fastForEach { if (it.invoke(screenX, screenY, pointer)) handled = true }
            return handled
        }

        override fun mouseMoved(screenX: Float, screenY: Float): Boolean {
            var handled = false
            mouseMoved.fastForEach { if (it.invoke(screenX, screenY)) handled = true }
            return handled
        }

        override fun scrolled(amountX: Float, amountY: Float): Boolean {
            var handled = false
            scrolled.fastForEach { if (it.invoke(amountX, amountY)) handled = true }
            return handled
        }

        override fun gamepadButtonPressed(button: GameButton, pressure: Float, gamepad: Int): Boolean {
            var handled = false
            gameButtonPressed.fastForEach { if (it.invoke(button, pressure, gamepad)) handled = true }
            return handled
        }

        override fun gamepadButtonReleased(button: GameButton, gamepad: Int): Boolean {
            var handled = false
            gameButtonReleased.fastForEach { if (it.invoke(button, gamepad)) handled = true }
            return handled
        }

        override fun gamepadJoystickMoved(stick: GameStick, xAxis: Float, yAxis: Float, gamepad: Int): Boolean {
            var handled = false
            gameJoystickMoved.fastForEach { if (it.invoke(stick, xAxis, yAxis, gamepad)) handled = true }
            return handled
        }

        override fun gamepadTriggerChanged(button: GameButton, pressure: Float, gamepad: Int): Boolean {
            var handled = false
            gamepadTriggerChanged.fastForEach { if (it.invoke(button, pressure, gamepad)) handled = true }
            return handled
        }
    }
}