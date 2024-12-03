package com.littlekt.file.font.ttf

import com.littlekt.file.ByteBuffer
import com.littlekt.file.font.ttf.table.*
import com.littlekt.graphics.g2d.font.TtfGlyph

/**
 * @author Colton Daily
 * @date 11/30/2021
 */
class TtfFontReader {

    private val tables: Tables = Tables()
    private var encoding: Encoding = DefaultEncoding(this)

    lateinit var glyphNames: GlyphNames
        private set
    lateinit var glyphs: GlyphSet
        private set
    var isCIDFont: Boolean = false
        private set
    var outlinesFormat: String = ""
        private set
    var unitsPerEm: Int = 0
        private set
    var ascender: Int = 0
        private set
    var descender: Int = 0
        private set
    var lineGap: Int = 0
        private set
    var advanceWidthMax: Int = 0
        private set
    var numberOfHMetrics: Int = 0
        private set
    var numGlyphs: Int = 0
        private set
    var capHeight: Int = 0
        private set
    var xMin: Float = 0f
        private set
    var yMin: Float = 0f
        private set
    var xMax: Float = 0f
        private set
    var yMax: Float = 0f
        private set

    operator fun get(codePoint: Int): TtfGlyph {
        return this[codePoint.toChar()]
    }

    operator fun get(char: Char): TtfGlyph {
        val glyphIndex: Int = encoding.charToGlyphIndex(char) ?: 0
        return glyphs[glyphIndex].let {
            it.calcPath()
            it.toImmutable()
        }
    }

    fun parse(buffer: ByteBuffer) {
        val numTables: Int
        val tableEntries: List<TableEntry>
        val signature: String = buffer.getString(0, 4)
        if (signature == charArrayOf(0.toChar(), 1.toChar(), 0.toChar(), 0.toChar()).concatToString() || signature == "true" || signature == "typ1") {
            outlinesFormat = "truetype"
            numTables = buffer.getUShort(4).toInt()
            tableEntries = parseOpenTypeTableEntries(buffer, numTables)
        } else if (signature == "OTTO") {
            outlinesFormat = "cff"
            numTables = buffer.getUShort(4).toInt()
            tableEntries = parseOpenTypeTableEntries(buffer, numTables)
        } else if (signature === "wOFF") {
            val flavor: String = buffer.getString(4, 4)
            outlinesFormat = when {
                flavor == charArrayOf(0.toChar(), 1.toChar(), 0.toChar(), 0.toChar()).concatToString() -> "truetype"
                flavor === "OTTO" -> "cff"
                else -> throw IllegalStateException("Unsupported OpenType flavor: $flavor")
            }
            numTables = buffer.getUShort(12).toInt()
            tableEntries = parseWOFFTableEntries(buffer, numTables)
        } else {
            throw IllegalStateException("Unsupported OpenType signature: $signature")
        }

        var indexToLocFormat: Int = 0
        var ltagTable: List<String>
        var cffTableEntry: TableEntry? = null
        var fvarTableEntry: TableEntry? = null
        var glyfTableEntry: TableEntry? = null
        var gdefTableEntry: TableEntry? = null
        var gposTableEntry: TableEntry? = null
        var gsubTableEntry: TableEntry? = null
        var hmtxTableEntry: TableEntry? = null
        var kernTableEntry: TableEntry? = null
        var locaTableEntry: TableEntry? = null
        var nameTableEntry: TableEntry? = null
        var metaTableEntry: TableEntry? = null
        var parser: Parser
        for (tableEntry in tableEntries) {
            val table: Table
            when (tableEntry.tag) {
                "cmap" -> {
                    table = uncompressTable(buffer, tableEntry)
                    val cmap: Cmap = CmapParser(table.buffer, table.offset).parse().also { tables.cmap = it }
                    encoding = CmapEncoding(cmap)
                }
                "cvt" -> {
                    table = uncompressTable(buffer, tableEntry)
                    parser = Parser(table.buffer, table.offset)
                    tables.cvt = parser.parseInt16List(tableEntry.length / 2)
                }
                "fvar" -> {
                    fvarTableEntry = tableEntry
                }
                "fpgm" -> {
                    table = uncompressTable(buffer, tableEntry)
                    parser = Parser(table.buffer, table.offset)
                    tables.fpgm = parser.parseByteList(tableEntry.length)
                }
                "head" -> {
                    table = uncompressTable(buffer, tableEntry)
                    tables.head = HeadParser(table.buffer, table.offset).parse().also {
                        xMin = it.xMin.toFloat()
                        xMax = it.xMax.toFloat()
                        yMin = it.yMin.toFloat()
                        yMax = it.yMax.toFloat()
                        unitsPerEm = it.unitsPerEm
                        indexToLocFormat = it.indexToLocFormat
                    }
                }
                "hhea" -> {
                    table = uncompressTable(buffer, tableEntry)
                    tables.hhea = HheaParser(table.buffer, table.offset).parse().also {
                        ascender = it.ascender
                        descender = it.descender
                        advanceWidthMax = it.advanceWidthMax
                        lineGap = it.lineGap
                        numberOfHMetrics = it.numberOfHMetrics
                    }
                }
                "hmtx" -> {
                    hmtxTableEntry = tableEntry
                }
                "ltag" -> {
                    table = uncompressTable(buffer, tableEntry)
                    ltagTable = LtagParser(table.buffer, table.offset).parse()
                }
                "maxp" -> {
                    table = uncompressTable(buffer, tableEntry)
                    tables.maxp = MaxpParser(table.buffer, table.offset).parse().also {
                        numGlyphs = it.numGlyphs
                    }
                }
                "name" -> {
                    nameTableEntry = tableEntry
                }
                "OS/2" -> {
                    table = uncompressTable(buffer, tableEntry)
                    val os2 = Os2Parser(table.buffer, table.offset).parse().also {
                        capHeight = it.sCapHeight
                    }
                    tables.os2 = os2
                }
                "post" -> {
                    table = uncompressTable(buffer, tableEntry)
                    tables.post = PostParser(table.buffer, table.offset).parse().also {
                        glyphNames = GlyphNames(it)
                    }
                }
                "prep" -> {
                    table = uncompressTable(buffer, tableEntry)
                    parser = Parser(table.buffer, table.offset)
                    tables.prep = parser.parseByteList(tableEntry.length)
                }
                "glyf" -> glyfTableEntry = tableEntry
                "loca" -> locaTableEntry = tableEntry
                "CFF " -> cffTableEntry = tableEntry
                "kern" -> kernTableEntry = tableEntry
                "GDEF" -> gdefTableEntry = tableEntry
                "GPOS" -> gposTableEntry = tableEntry
                "GSUB" -> gsubTableEntry = tableEntry
                "meta" -> metaTableEntry = tableEntry
            }
        }
        // TODO Determine if name table is needed

        if (glyfTableEntry != null && locaTableEntry != null) {
            val glyf: TableEntry = glyfTableEntry
            val loca: TableEntry = locaTableEntry
            val shortVersion: Boolean = indexToLocFormat == 0
            val locaTable: Table = uncompressTable(buffer, loca)
            val locaOffsets: IntArray = LocaParser(locaTable.buffer, locaTable.offset, numGlyphs, shortVersion).parse()
            val glyfTable: Table = uncompressTable(buffer, glyf)
            glyphs = GlyfParser(glyfTable.buffer, glyfTable.offset, locaOffsets, this).parse()
        } else if (cffTableEntry != null) {
            // TODO CFF table entry
        } else {
            throw RuntimeException("Font doesn't contain TrueType or CFF outlines.")
        }
        val hmtxTable: Table = uncompressTable(buffer, hmtxTableEntry ?: throw RuntimeException("hmtx table entry was not found!"))
        HmtxParser(hmtxTable.buffer, hmtxTable.offset, numberOfHMetrics, numGlyphs, glyphs).parse()
        addGlyphNames()
    }

    private fun parseOpenTypeTableEntries(buffer: ByteBuffer, numTables: Int): List<TableEntry> {
        val tableEntries: MutableList<TableEntry> = mutableListOf()
        var p: Int = 12
        repeat(numTables) {
            val tag: String = buffer.getString(p, 4)
            val checksum: Int = buffer.getUInt(p + 4).toInt()
            val offset: Int = buffer.getUInt(p + 8).toInt()
            val length: Int = buffer.getUInt(p + 12).toInt()
            tableEntries += TableEntry(tag, checksum, offset, length, Compression.NONE, 0)
            p += 16
        }
        return tableEntries.toList()
    }

    private fun parseWOFFTableEntries(buffer: ByteBuffer, numTables: Int): List<TableEntry> {
        return emptyList() // TODO("Not yet implemented")
    }

    private fun uncompressTable(buffer: ByteBuffer, tableEntry: TableEntry): Table {
        if (tableEntry.compression == Compression.WOFF) {
            // TODO impl inflating WOFF compression
        }
        return Table(buffer, tableEntry.offset)
    }

    private fun addGlyphNames() {
        val glyphIndexMap: Map<Int, Int> = tables.cmap?.glyphIndexMap ?: error("Unable to add glyph name due to cmap table being null")
        val codes: Set<Int> = glyphIndexMap.keys
        for (code in codes) {
            val index: Int = glyphIndexMap[code] ?: 0
            glyphs[index].apply { addUnicode(code) }
        }
        for ((index, glyph) in glyphs.withIndex()) {
            if (encoding is CffEncoding) {
                glyph.name = when {
                    isCIDFont -> "gid$index"
                    else -> (encoding as CffEncoding).charset[index].toString()
                }
            } else if (glyphNames.names.isNotEmpty()) {
                glyph.name = glyphNames.names[index]
            }
        }
    }

    override fun toString(): String {
        return "TtfFont(outlinesFormat='$outlinesFormat', tables=$tables, encoding=$encoding, unitsPerEm=$unitsPerEm, ascender=$ascender, descender=$descender, numberOfHMetrics=$numberOfHMetrics, numGlyphs=$numGlyphs, glyphNames=$glyphNames, glyphs=$glyphs)"
    }

}

enum class Compression {
    WOFF,
    NONE
}

data class TableEntry(val tag: String, val checksum: Int, val offset: Int, val length: Int, val compression: Compression, val compressedLength: Int)

data class Table(val buffer: ByteBuffer, val offset: Int)

class Tables {
    var head: Head? = null
    var cmap: Cmap? = null
    var cvt: ShortArray? = null
    var fpgm: ByteArray? = null
    var hhea: Hhea? = null
    var maxp: Maxp? = null
    var os2: Os2? = null
    var post: Post? = null
    var prep: ByteArray? = null
    override fun toString(): String = "Tables(head=$head, cmap=$cmap, hhea=$hhea, maxp=$maxp, os2=$os2, post=$post"
}
