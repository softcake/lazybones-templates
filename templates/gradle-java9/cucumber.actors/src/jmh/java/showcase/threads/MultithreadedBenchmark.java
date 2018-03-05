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

package showcase.threads;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Benchmarks using multithreaded JMH facilities.
 */
@Fork(value = 1)
@Warmup(iterations = 0)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Group)
public class MultithreadedBenchmark {

    private final AtomicLong _counter = new AtomicLong();

    @Group("with1Thread")
    @GroupThreads(1)
    @Benchmark
    public void increment1() {
        _counter.incrementAndGet();
    }

    @Group("with1Thread")
    @GroupThreads(1)
    @Benchmark
    public long read1() {
        return _counter.get();
    }

    @Group("with2Threads")
    @GroupThreads(2)
    @Benchmark
    public void increment2() {
        _counter.incrementAndGet();
    }

    @Group("with2Threads")
    @GroupThreads(2)
    @Benchmark
    public long read2() {
        return _counter.get();
    }

    @Group("with4Threads")
    @GroupThreads(4)
    @Benchmark
    public void increment4() {
        _counter.incrementAndGet();
    }

    @Group("with4Threads")
    @GroupThreads(4)
    @Benchmark
    public long read4() {
        return _counter.get();
    }

}
