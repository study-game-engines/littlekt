package com.littlekt.file.font.ttf

import com.littlekt.file.ByteBuffer

internal enum class Type(val size: Int) {
    BYTE(1),
    SHORT(2),
    INT(4),
    FLOAT(4),
    LONG_DATE_TIME(8),
    TAG(4)
}

internal class Parser(private val buffer: ByteBuffer, val offset: Int) {

    var relativeOffset: Int = 0

    fun parseUByte() = buffer.getUByte(offset + relativeOffset++).toInt()
    fun parseByte(): Byte = buffer.getByte(offset + relativeOffset++)
    fun parseChar() = buffer.getByte(offset + relativeOffset++).toInt().toChar()
    fun getParseCard8() = parseUByte()
    fun getParseUint16() = buffer.getUShort(offset + relativeOffset).also { relativeOffset += 2 }.toUShort().toInt()
    fun getParseCard16() = getParseUint16()
    fun getParseOffset16() = getParseUint16()
    fun getParseInt16() = buffer.getShort(offset + relativeOffset).also { relativeOffset += 2 }
    fun getParseF2Dot14() = (buffer.getShort(offset + relativeOffset) / 16384).also { relativeOffset += 2 }
    fun getParseUint32() = buffer.getUInt(offset + relativeOffset).also { relativeOffset += 4 }
    fun getParseInt32() = buffer.getUInt(offset + relativeOffset).also { relativeOffset += 4 }.toInt()
    fun getParseOffset32() = getParseUint32()

    fun getParseFixed(): Float {
        val decimal: Short = buffer.getShort(offset)
        val fraction: Short = buffer.getUShort(offset + 2).toShort()
        relativeOffset += 4
        return (decimal + fraction / 65535).toFloat()
    }

    fun parseString(length: Int): String {
        val offset: Int = offset + relativeOffset
        var string: String = ""
        relativeOffset += length
        for (index in 0 until length) {
            string += buffer.getUByte(offset + index).toInt().toChar()
        }
        return string
    }

    fun getParseLongDateTime() = buffer.getInt(offset + relativeOffset + 4).run { this - 2082844800 }.also { relativeOffset += 8 }

    fun parseVersion(minorBase: Int = 0x1000): Float {
        val major: Short = buffer.getUShort(offset + relativeOffset).toShort()
        val minor: Short = buffer.getUShort(offset + relativeOffset + 2).also { relativeOffset += 4 }.toShort()
        return major + minor / minorBase / 10f
    }

    fun skip(type: Type, amount: Int = 1) {
        relativeOffset += type.size * amount
    }

    fun parseUInt32List(count: Int? = null): IntArray {
        val total: Int = count ?: getParseUint32().toInt()
        val offsets: IntArray = IntArray(total)
        var offset: Int = offset + relativeOffset
        for (index in 0 until total) {
            offsets[index] = buffer.getUInt(offset).toInt()
            offset += 4
        }
        relativeOffset += total * 4
        return offsets
    }

    fun parseUint16List(count: Int? = null): ShortArray {
        val total: Int = count ?: getParseUint32().toInt()
        val offsets: ShortArray = ShortArray(total)
        var offset: Int = offset + relativeOffset
        for (i in 0 until total) {
            offsets[i] = buffer.getUShort(offset).toShort()
            offset += 2
        }
        relativeOffset += total * 2
        return offsets
    }

    fun parseOffset16List(count: Int? = null) = parseUint16List(count)

    fun parseInt16List(count: Int? = null): ShortArray {
        val total: Int = count ?: getParseUint32().toInt()
        val offsets: ShortArray = ShortArray(total)
        var offset: Int = offset + relativeOffset
        for (index in 0 until total) {
            offsets[index] = buffer.getShort(offset)
            offset += 2
        }
        relativeOffset += total * 2
        return offsets
    }

    fun parseByteList(count: Int): ByteArray {
        val list: ByteArray = ByteArray(count)
        var offset: Int = offset + relativeOffset
        for (index in 0 until count) {
            list[index] = buffer.getUByte(offset++).toByte()
        }
        relativeOffset += count
        return list
    }

}
