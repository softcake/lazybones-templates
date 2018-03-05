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
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Benchmarks using multithreaded JMH facilities.
 */
@Fork(value = 1)
@Warmup(iterations = 0)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class TheMoreThreadsTheSlowerBenchmark {

    private final WorkPool _workPool = new WorkPool();

    @Benchmark
    @Threads(1)
    public void with001Threads() throws Exception {
        _workPool.doWork();
    }

    @Benchmark
    @Threads(5)
    public void with005Threads() throws Exception {
        _workPool.doWork();
    }

    @Benchmark
    @Threads(25)
    public void with025Threads() throws Exception {
        _workPool.doWork();
    }

    @Benchmark
    @Threads(50)
    public void with050Threads() throws Exception {
        _workPool.doWork();
    }

    public static class WorkPool {

        private final AtomicInteger _threadsDoingWork = new AtomicInteger();

        public void doWork() throws InterruptedException {
            int threadsDoingWork = _threadsDoingWork.getAndIncrement();
            try {
                Thread.sleep(50 + threadsDoingWork * 10);
            } finally {
                _threadsDoingWork.decrementAndGet();
            }

        }
    }

}
