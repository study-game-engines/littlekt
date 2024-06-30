// Generated by jextract

package com.littlekt.wgpu;

import java.lang.invoke.*;
import java.lang.foreign.*;
import java.nio.ByteOrder;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.lang.foreign.ValueLayout.*;
import static java.lang.foreign.MemoryLayout.PathElement.*;

/**
 * {@snippet lang=c :
 * struct WGPURenderBundleEncoderDescriptor {
 *     const WGPUChainedStruct *nextInChain;
 *     const char *label;
 *     size_t colorFormatCount;
 *     const WGPUTextureFormat *colorFormats;
 *     WGPUTextureFormat depthStencilFormat;
 *     uint32_t sampleCount;
 *     WGPUBool depthReadOnly;
 *     WGPUBool stencilReadOnly;
 * }
 * }
 */
public class WGPURenderBundleEncoderDescriptor {

    WGPURenderBundleEncoderDescriptor() {
        // Should not be called directly
    }

    private static final GroupLayout $LAYOUT = MemoryLayout.structLayout(
        WGPU.C_POINTER.withName("nextInChain"),
        WGPU.C_POINTER.withName("label"),
        WGPU.C_LONG_LONG.withName("colorFormatCount"),
        WGPU.C_POINTER.withName("colorFormats"),
        WGPU.C_INT.withName("depthStencilFormat"),
        WGPU.C_INT.withName("sampleCount"),
        WGPU.C_INT.withName("depthReadOnly"),
        WGPU.C_INT.withName("stencilReadOnly")
    ).withName("WGPURenderBundleEncoderDescriptor");

    /**
     * The layout of this struct
     */
    public static final GroupLayout layout() {
        return $LAYOUT;
    }

    private static final AddressLayout nextInChain$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("nextInChain"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const WGPUChainedStruct *nextInChain
     * }
     */
    public static final AddressLayout nextInChain$layout() {
        return nextInChain$LAYOUT;
    }

    private static final long nextInChain$OFFSET = 0;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const WGPUChainedStruct *nextInChain
     * }
     */
    public static final long nextInChain$offset() {
        return nextInChain$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const WGPUChainedStruct *nextInChain
     * }
     */
    public static MemorySegment nextInChain(MemorySegment struct) {
        return struct.get(nextInChain$LAYOUT, nextInChain$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const WGPUChainedStruct *nextInChain
     * }
     */
    public static void nextInChain(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(nextInChain$LAYOUT, nextInChain$OFFSET, fieldValue);
    }

    private static final AddressLayout label$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("label"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const char *label
     * }
     */
    public static final AddressLayout label$layout() {
        return label$LAYOUT;
    }

    private static final long label$OFFSET = 8;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const char *label
     * }
     */
    public static final long label$offset() {
        return label$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const char *label
     * }
     */
    public static MemorySegment label(MemorySegment struct) {
        return struct.get(label$LAYOUT, label$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const char *label
     * }
     */
    public static void label(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(label$LAYOUT, label$OFFSET, fieldValue);
    }

    private static final OfLong colorFormatCount$LAYOUT = (OfLong)$LAYOUT.select(groupElement("colorFormatCount"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * size_t colorFormatCount
     * }
     */
    public static final OfLong colorFormatCount$layout() {
        return colorFormatCount$LAYOUT;
    }

    private static final long colorFormatCount$OFFSET = 16;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * size_t colorFormatCount
     * }
     */
    public static final long colorFormatCount$offset() {
        return colorFormatCount$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * size_t colorFormatCount
     * }
     */
    public static long colorFormatCount(MemorySegment struct) {
        return struct.get(colorFormatCount$LAYOUT, colorFormatCount$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * size_t colorFormatCount
     * }
     */
    public static void colorFormatCount(MemorySegment struct, long fieldValue) {
        struct.set(colorFormatCount$LAYOUT, colorFormatCount$OFFSET, fieldValue);
    }

    private static final AddressLayout colorFormats$LAYOUT = (AddressLayout)$LAYOUT.select(groupElement("colorFormats"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * const WGPUTextureFormat *colorFormats
     * }
     */
    public static final AddressLayout colorFormats$layout() {
        return colorFormats$LAYOUT;
    }

    private static final long colorFormats$OFFSET = 24;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * const WGPUTextureFormat *colorFormats
     * }
     */
    public static final long colorFormats$offset() {
        return colorFormats$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * const WGPUTextureFormat *colorFormats
     * }
     */
    public static MemorySegment colorFormats(MemorySegment struct) {
        return struct.get(colorFormats$LAYOUT, colorFormats$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * const WGPUTextureFormat *colorFormats
     * }
     */
    public static void colorFormats(MemorySegment struct, MemorySegment fieldValue) {
        struct.set(colorFormats$LAYOUT, colorFormats$OFFSET, fieldValue);
    }

    private static final OfInt depthStencilFormat$LAYOUT = (OfInt)$LAYOUT.select(groupElement("depthStencilFormat"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * WGPUTextureFormat depthStencilFormat
     * }
     */
    public static final OfInt depthStencilFormat$layout() {
        return depthStencilFormat$LAYOUT;
    }

    private static final long depthStencilFormat$OFFSET = 32;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * WGPUTextureFormat depthStencilFormat
     * }
     */
    public static final long depthStencilFormat$offset() {
        return depthStencilFormat$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * WGPUTextureFormat depthStencilFormat
     * }
     */
    public static int depthStencilFormat(MemorySegment struct) {
        return struct.get(depthStencilFormat$LAYOUT, depthStencilFormat$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * WGPUTextureFormat depthStencilFormat
     * }
     */
    public static void depthStencilFormat(MemorySegment struct, int fieldValue) {
        struct.set(depthStencilFormat$LAYOUT, depthStencilFormat$OFFSET, fieldValue);
    }

    private static final OfInt sampleCount$LAYOUT = (OfInt)$LAYOUT.select(groupElement("sampleCount"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * uint32_t sampleCount
     * }
     */
    public static final OfInt sampleCount$layout() {
        return sampleCount$LAYOUT;
    }

    private static final long sampleCount$OFFSET = 36;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * uint32_t sampleCount
     * }
     */
    public static final long sampleCount$offset() {
        return sampleCount$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * uint32_t sampleCount
     * }
     */
    public static int sampleCount(MemorySegment struct) {
        return struct.get(sampleCount$LAYOUT, sampleCount$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * uint32_t sampleCount
     * }
     */
    public static void sampleCount(MemorySegment struct, int fieldValue) {
        struct.set(sampleCount$LAYOUT, sampleCount$OFFSET, fieldValue);
    }

    private static final OfInt depthReadOnly$LAYOUT = (OfInt)$LAYOUT.select(groupElement("depthReadOnly"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * WGPUBool depthReadOnly
     * }
     */
    public static final OfInt depthReadOnly$layout() {
        return depthReadOnly$LAYOUT;
    }

    private static final long depthReadOnly$OFFSET = 40;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * WGPUBool depthReadOnly
     * }
     */
    public static final long depthReadOnly$offset() {
        return depthReadOnly$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * WGPUBool depthReadOnly
     * }
     */
    public static int depthReadOnly(MemorySegment struct) {
        return struct.get(depthReadOnly$LAYOUT, depthReadOnly$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * WGPUBool depthReadOnly
     * }
     */
    public static void depthReadOnly(MemorySegment struct, int fieldValue) {
        struct.set(depthReadOnly$LAYOUT, depthReadOnly$OFFSET, fieldValue);
    }

    private static final OfInt stencilReadOnly$LAYOUT = (OfInt)$LAYOUT.select(groupElement("stencilReadOnly"));

    /**
     * Layout for field:
     * {@snippet lang=c :
     * WGPUBool stencilReadOnly
     * }
     */
    public static final OfInt stencilReadOnly$layout() {
        return stencilReadOnly$LAYOUT;
    }

    private static final long stencilReadOnly$OFFSET = 44;

    /**
     * Offset for field:
     * {@snippet lang=c :
     * WGPUBool stencilReadOnly
     * }
     */
    public static final long stencilReadOnly$offset() {
        return stencilReadOnly$OFFSET;
    }

    /**
     * Getter for field:
     * {@snippet lang=c :
     * WGPUBool stencilReadOnly
     * }
     */
    public static int stencilReadOnly(MemorySegment struct) {
        return struct.get(stencilReadOnly$LAYOUT, stencilReadOnly$OFFSET);
    }

    /**
     * Setter for field:
     * {@snippet lang=c :
     * WGPUBool stencilReadOnly
     * }
     */
    public static void stencilReadOnly(MemorySegment struct, int fieldValue) {
        struct.set(stencilReadOnly$LAYOUT, stencilReadOnly$OFFSET, fieldValue);
    }

    /**
     * Obtains a slice of {@code arrayParam} which selects the array element at {@code index}.
     * The returned segment has address {@code arrayParam.address() + index * layout().byteSize()}
     */
    public static MemorySegment asSlice(MemorySegment array, long index) {
        return array.asSlice(layout().byteSize() * index);
    }

    /**
     * The size (in bytes) of this struct
     */
    public static long sizeof() { return layout().byteSize(); }

    /**
     * Allocate a segment of size {@code layout().byteSize()} using {@code allocator}
     */
    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout());
    }

    /**
     * Allocate an array of size {@code elementCount} using {@code allocator}.
     * The returned segment has size {@code elementCount * layout().byteSize()}.
     */
    public static MemorySegment allocateArray(long elementCount, SegmentAllocator allocator) {
        return allocator.allocate(MemoryLayout.sequenceLayout(elementCount, layout()));
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, Arena arena, Consumer<MemorySegment> cleanup) {
        return reinterpret(addr, 1, arena, cleanup);
    }

    /**
     * Reinterprets {@code addr} using target {@code arena} and {@code cleanupAction} (if any).
     * The returned segment has size {@code elementCount * layout().byteSize()}
     */
    public static MemorySegment reinterpret(MemorySegment addr, long elementCount, Arena arena, Consumer<MemorySegment> cleanup) {
        return addr.reinterpret(layout().byteSize() * elementCount, arena, cleanup);
    }
}

