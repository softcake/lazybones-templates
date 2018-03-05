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

@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ReadDoubleBenchmark {

    private final ByteBuffer _byteBuffer = ByteBuffer.allocate(1024).order(ByteOrder.LITTLE_ENDIAN);

    @Setup
    public void setUp() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            _byteBuffer.putDouble(i);
        }
        _byteBuffer.flip();
    }

    @Benchmark
    public void getDouble() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            double value = _byteBuffer.getDouble();
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }

    @Benchmark
    public void getLongToDouble() {
        for (int i = 0; i < _byteBuffer.capacity() / 8; i++) {
            double value = Double.longBitsToDouble(_byteBuffer.getLong());
            assertEquals(value, i);
        }
        _byteBuffer.rewind();
    }
}
