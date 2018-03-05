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

import com.google.common.base.Optional;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;


@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class NoOpStrategyBenchmark {

    private final Filterable _noOpFilterable = Filterable.NO_OP_FILTERABLE;
    private final Filterable _nullFilterable = null;
    private final Optional<Filterable> _optionalFilterable = Optional.absent();
    private final Filterable _dropEvenFilterable = new DropEvenValues();

    private long _currentValue;

    @TearDown
    public void tearDown() {
        assert _currentValue > 0 : String.format(
            "Expected _currentValue be greater then 0 but was %s",
            _currentValue);
    }

    @Benchmark
    public boolean nullCheck() {
        final boolean dropValue;
        if (_nullFilterable == null) {
            dropValue = false;
        } else {
            dropValue = _nullFilterable.dropValue(_currentValue);
        }
        _currentValue++;
        return dropValue;
    }

    @Benchmark
    public boolean optional() {
        final boolean dropValue;
        if (_optionalFilterable.isPresent()) {
            dropValue = _nullFilterable.dropValue(_currentValue);
        } else {
            dropValue = false;
        }
        _currentValue++;
        return dropValue;
    }

    @Benchmark
    public boolean noOpImplementation() {
        return _noOpFilterable.dropValue(_currentValue++);
    }

    @Benchmark
    public boolean someImplementation() {
        return _dropEvenFilterable.dropValue(_currentValue++);
    }


    private interface Filterable {

        Filterable NO_OP_FILTERABLE = i -> false;

        boolean dropValue(long i);
    }

    private static class DropEvenValues implements Filterable {

        @Override
        public boolean dropValue(final long i) {
            return i % 2 == 0;
        }

    }

}
