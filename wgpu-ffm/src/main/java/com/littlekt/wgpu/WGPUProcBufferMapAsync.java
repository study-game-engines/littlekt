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
 * typedef void (*WGPUProcBufferMapAsync)(WGPUBuffer, WGPUMapModeFlags, size_t, size_t, WGPUBufferMapCallback, void *)
 * }
 */
public class WGPUProcBufferMapAsync {

    WGPUProcBufferMapAsync() {
        // Should not be called directly
    }

    /**
     * The function pointer signature, expressed as a functional interface
     */
    public interface Function {
        void apply(MemorySegment buffer, int mode, long offset, long size, MemorySegment callback, MemorySegment userdata);
    }

    private static final FunctionDescriptor $DESC = FunctionDescriptor.ofVoid(
        WGPU.C_POINTER,
        WGPU.C_INT,
        WGPU.C_LONG_LONG,
        WGPU.C_LONG_LONG,
        WGPU.C_POINTER,
        WGPU.C_POINTER
    );

    /**
     * The descriptor of this function pointer
     */
    public static FunctionDescriptor descriptor() {
        return $DESC;
    }

    private static final MethodHandle UP$MH = WGPU.upcallHandle(WGPUProcBufferMapAsync.Function.class, "apply", $DESC);

    /**
     * Allocates a new upcall stub, whose implementation is defined by {@code fi}.
     * The lifetime of the returned segment is managed by {@code arena}
     */
    public static MemorySegment allocate(WGPUProcBufferMapAsync.Function fi, Arena arena) {
        return Linker.nativeLinker().upcallStub(UP$MH.bindTo(fi), $DESC, arena);
    }

    private static final MethodHandle DOWN$MH = Linker.nativeLinker().downcallHandle($DESC);

    /**
     * Invoke the upcall stub {@code funcPtr}, with given parameters
     */
    public static void invoke(MemorySegment funcPtr,MemorySegment buffer, int mode, long offset, long size, MemorySegment callback, MemorySegment userdata) {
        try {
             DOWN$MH.invokeExact(funcPtr, buffer, mode, offset, size, callback, userdata);
        } catch (Throwable ex$) {
            throw new AssertionError("should not reach here", ex$);
        }
    }
}

