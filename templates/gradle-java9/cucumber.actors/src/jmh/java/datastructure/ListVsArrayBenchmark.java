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

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * .
 * @author The softcake Authors.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class ListVsArrayBenchmark {

    private final List<Integer> _list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
    private final Integer[] _array = new Integer[]{1, 2, 3, 4};

    @Benchmark
    public int list_forEach() {
        int sum = 0;
        for (Integer number : _list) {
            sum += number;
        }
        return sum;
    }

    @Benchmark
    public int list_index() {
        int sum = 0;
        for (int i = 0; i < _list.size(); i++) {
            sum += _list.get(i);
        }
        return sum;
    }

    @Benchmark
    public int array() {
        int sum = 0;
        for (int i = 0; i < _array.length; i++) {
            sum += _array[i];
        }
        return sum;
    }

}
