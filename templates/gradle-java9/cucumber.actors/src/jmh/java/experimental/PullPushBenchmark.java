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

package experimental;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

@Fork(value = 2)
@Warmup(iterations = 7)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class PullPushBenchmark {

    private static final int MAX = 100_000_000;
    private final int[] _nulls = new int[MAX / 100];

    @Setup
    public void setup() {
        int current = 0;
        for (int i = 0; i < MAX; i++) {
            if (i % 100 == 0) {
                _nulls[current++] = i;
            }
        }
    }

    @Benchmark
    public long push() {
        Reader reader = new Reader(MAX, _nulls);
        PushConsumer consumer = new PushConsumer();
        for (int i = 0; i < reader.getMax(); i++) {
            boolean nextIsNull = reader.nextIsNull();
            if (nextIsNull) {
                consumer.consumeNull();
            } else {
                consumer.consume(reader.readNext());
            }
        }
        assertEquals(consumer.getSum(), 4950000000000000L);
        assertEquals(consumer.getNullCount(), 1000000);
        return consumer.getSum();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.EXCLUDE)
    public long pushWithoutJit() {
        Reader reader = new Reader(MAX, _nulls);
        PushConsumer consumer = new PushConsumer();
        for (int i = 0; i < reader.getMax(); i++) {
            boolean nextIsNull = reader.nextIsNull();
            if (nextIsNull) {
                consumer.consumeNull();
            } else {
                consumer.consume(reader.readNext());
            }
        }
        assertEquals(consumer.getSum(), 4950000000000000L);
        assertEquals(consumer.getNullCount(), 1000000);
        return consumer.getSum();
    }

    @Benchmark
    public long pull() {
        Reader reader = new Reader(MAX, _nulls);
        PullConsumer consumer = new PullConsumer();
        consumer.consume(reader);
        assertEquals(consumer.getSum(), 4950000000000000L);
        assertEquals(consumer.getNullCount(), 1000000);
        return consumer.getSum();
    }

    @Benchmark
    @CompilerControl(CompilerControl.Mode.EXCLUDE)
    public long pullWithoutJit() {
        Reader reader = new Reader(MAX, _nulls);
        PullConsumer consumer = new PullConsumer();
        consumer.consume(reader);
        assertEquals(consumer.getSum(), 4950000000000000L);
        assertEquals(consumer.getNullCount(), 1000000);
        return consumer.getSum();
    }

    public static class Reader {

        private final int _max;
        private final int[] _nulls;
        private int _current;
        private int _currentNull;
        private boolean _nullsDepleted;

        public Reader(final int max, final int[] nulls) {
            _max = max;
            _nulls = nulls;
        }

        public long getMax() {
            return _max;
        }

        public boolean nextIsNull() {
            if ((!_nullsDepleted) && _nulls[_currentNull] == _current) {
                _current++;
                _currentNull++;
                if (_currentNull == _nulls.length) {
                    _nullsDepleted = true;
                }
                return true;
            }
            return false;
        }

        public int readNext() {
            return _current++;
        }
    }

    public static class PushConsumer {

        private long _sum;
        private int _nullCount;

        public void consume(final int number) {
            _sum += number;
        }

        public void consumeNull() {
            _nullCount++;
        }

        public long getSum() {
            return _sum;
        }

        public int getNullCount() {
            return _nullCount;
        }
    }

    public static class PullConsumer {

        private long _sum;
        private int _nullCount;

        public void consume(final Reader reader) {
            for (int i = 0; i < reader.getMax(); i++) {
                boolean nextIsNull = reader.nextIsNull();
                if (nextIsNull) {
                    _nullCount++;
                } else {
                    _sum += reader.readNext();
                }
            }
        }

        public long getSum() {
            return _sum;
        }

        public int getNullCount() {
            return _nullCount;
        }
    }

}
