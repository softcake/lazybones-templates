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

package datastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.BitSet;
import java.util.concurrent.TimeUnit;

/**
 * Benchmarking different approaches to maintain an index of null values.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class NullIndexBenchmark {

    private static final int MAX = 100_000_000;
    private static final int EVERY_NULL_ELEMENT = 100;
    private final int[] _nullsIndices = new int[MAX / EVERY_NULL_ELEMENT];
    private final boolean[] _nullsFlags = new boolean[MAX];
    private final BitSet _nullsBitSet = new BitSet(MAX);

    @Setup
    public void setup() {
        int current = 0;
        for (int i = 0; i < MAX; i++) {
            if (i % EVERY_NULL_ELEMENT == 0) {
                _nullsIndices[current] = i;
                _nullsFlags[i] = true;
                _nullsBitSet.set(i);
                current++;
            }
        }
    }

    @Benchmark
    public int arrayWithNullIndices() {
        int nullCount = 0;
        boolean _nullsDepleted = false;
        for (int i = 0; i < MAX; i++) {
            if ((!_nullsDepleted) && _nullsIndices[nullCount] == i) {
                nullCount++;
                if (nullCount == _nullsIndices.length) {
                    _nullsDepleted = true;
                }
            }
        }
        assertEquals(nullCount, 1000000);
        return nullCount;
    }

    @Benchmark
    public long arrayWithBooleans() {
        int nullCount = 0;
        for (int i = 0; i < MAX; i++) {
            if (_nullsFlags[i]) {
                nullCount++;
            }
        }

        assertEquals(nullCount, 1000000);
        return nullCount;
    }

    @Benchmark
    public long bitSet() {
        int nullCount = 0;
        for (int i = 0; i < MAX; i++) {
            if (_nullsBitSet.get(i)) {
                nullCount++;
            }
        }

        assertEquals(nullCount, 1000000);
        return nullCount;
    }

}
