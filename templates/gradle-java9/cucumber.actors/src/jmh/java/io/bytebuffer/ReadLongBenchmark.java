/*
 *
 * Copyright 2018 softcake.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.bytebuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;

@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ReadLongBenchmark {

    private final ByteBuffer _byteBuffer = ByteBuffer.allocate(64 * 1024)
                                                     .order(ByteOrder.LITTLE_ENDIAN);

    @Setup
    public void setUp() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            _byteBuffer.putLong(i);
        }
        _byteBuffer.flip();
    }

    @Benchmark
    public void getLong() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = _byteBuffer.getLong();
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void getLong_reverse() {
        _byteBuffer.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = Long.reverseBytes(_byteBuffer.getLong());
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void getLongWithIndex() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = _byteBuffer.getLong(i * 8);
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void longBuffer() {
        LongBuffer longBuffer = _byteBuffer.asLongBuffer();
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = longBuffer.get();
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }

    @SuppressWarnings("booleanexpressioncomplexity")
    @Benchmark
    public void oneBytesArray() {
        byte[] bytes = new byte[_byteBuffer.remaining()];
        _byteBuffer.get(bytes);
        for (int i = 0; i < bytes.length / 8; i++) {
            int offset = i * 8;
            long value = ((long) bytes[offset + 7] << 56)
                         + ((long) (bytes[offset + 6] & 255) << 48)
                         + ((long) (bytes[offset + 5] & 255) << 40)
                         + ((long) (bytes[offset + 4] & 255) << 32)
                         + ((long) (bytes[offset + 3] & 255) << 24)
                         + ((bytes[offset + 2] & 255) << 16)
                         + ((bytes[offset + 1] & 255) << 8)
                         + ((bytes[offset] & 255));
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }

    @SuppressWarnings("booleanexpressioncomplexity")
    @Benchmark
    public void chunkedBytesArray() {
        byte[] longChunk = new byte[8];
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            _byteBuffer.get(longChunk);
            final long value = ((long) longChunk[7] << 56)
                               + ((long) (longChunk[6] & 255) << 48)
                               + ((long) (longChunk[5] & 255) << 40)
                               + ((long) (longChunk[4] & 255) << 32)
                               + ((long) (longChunk[3] & 255) << 24)
                               + ((longChunk[2] & 255) << 16)
                               + ((longChunk[1] & 255) << 8)
                               + (longChunk[0] & 255);
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }

    @SuppressWarnings("booleanexpressioncomplexity")
    @Benchmark
    public void singleBytes() {
        int offset = 0;
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            long value = ((long) _byteBuffer.get(offset + 7) << 56)
                         + ((long) (_byteBuffer.get(offset + 6) & 255) << 48)
                         + ((long) (_byteBuffer.get(offset + 5) & 255) << 40)
                         + ((long) (_byteBuffer.get(offset + 4) & 255) << 32)
                         + ((long) (_byteBuffer.get(offset + 3) & 255) << 24)
                         + ((_byteBuffer.get(offset + 2) & 255) << 16)
                         + ((_byteBuffer.get(offset + 1) & 255) << 8)
                         + ((_byteBuffer.get(offset) & 255) << 0);
            offset += 8;
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }

}
