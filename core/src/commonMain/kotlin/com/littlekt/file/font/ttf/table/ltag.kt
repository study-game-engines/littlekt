package com.littlekt.file.font.ttf.table

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.Parser
import com.littlekt.file.font.ttf.Type

// The `ltag` table stores IETF BCP-47 language tags. It allows supporting The `ltag` table stores IETF BCP-47 language tags. It allows supporting
// https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6ltag.html
// http://www.w3.org/International/articles/language-tags/
// http://www.iana.org/assignments/language-subtag-registry/language-subtag-registry
internal class LtagParser(val buffer: ByteBuffer, val start: Int) {

    fun parse(): List<String> {
        val parser: Parser = Parser(buffer, start)
        val tableVersion: Int = parser.getParseUint32().toInt()
        check(tableVersion == 1) { "Unsupported table version" }
        parser.skip(Type.INT)
        val numTags: Int = parser.getParseUint32().toInt()
        val tags: MutableList<String> = mutableListOf()
        repeat(numTags) {
            var tag: String = ""
            val offset: Int = start + parser.getParseUint16()
            val length: Int = parser.getParseUint16()
            for (j in offset + 1 until offset + length) {
                tag += buffer.getByte(j).toInt().toChar()
            }
            tags += tag
        }
        return tags.toList()
    }

}
