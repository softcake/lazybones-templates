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

import com.google.common.collect.ImmutableList;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.ArrayList;
import java.util.List;

/**
 * .
 * @author The softcake Authors.
 */
@Fork(value = 2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class ListCreationBenchmark {

    private static final int ELEMENT_COUNT = 16;
    private final List<String> _list = new ArrayList<>(ELEMENT_COUNT);

    @Benchmark
    public List<String> arrayList() {
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i + "");
        }
        return arrayList;
    }

    @Benchmark
    public List<String> arrayList_preSized() {
        List<String> arrayList = new ArrayList<>(ELEMENT_COUNT);
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i + "");
        }
        return arrayList;
    }

    @Benchmark
    public List<String> arrayList_preSized_reUsed() {
        _list.clear();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            _list.add(i + "");
        }
        return _list;
    }

    @Benchmark
    public List<String> immutableList() {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            builder.add(i + "");
        }
        return builder.build();
    }
}
